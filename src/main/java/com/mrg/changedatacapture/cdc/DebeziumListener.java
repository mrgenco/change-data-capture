package com.mrg.changedatacapture.cdc;

import com.mrg.changedatacapture.customer.CustomerRatingsService;
import io.debezium.config.Configuration;

import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.*;

@Slf4j
@Component
public class DebeziumListener {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final CustomerRatingsService customerRatingsService;

    public DebeziumListener(Configuration customerConnectorConfiguration, CustomerRatingsService customerRatingsService) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(customerConnectorConfiguration.asProperties())
                .notifying(this::handleChangeEvent)
                .build();
        this.customerRatingsService = customerRatingsService;
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        try{

            SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
            log.info(sourceRecord.toString());
            log.info("=======================");
            log.info("Key = '" + sourceRecord.key() + "' value = '" + sourceRecord.value() + "'");

            Struct sourceRecordChangeValue= (Struct) sourceRecord.value();

            if (sourceRecordChangeValue != null && sourceRecordChangeValue.schema().name().equals("topic-customer.cdc.CustomerRatings.Envelope")) {
                Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

                if(operation != Envelope.Operation.READ) {
                    String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER; // Handling Update & Insert operations.

                    Struct struct = (Struct) sourceRecordChangeValue.get(record);

                    Map<String, Object> payload = struct.schema().fields().stream()
                            .map(Field::name)
                            .filter(fieldName -> struct.get(fieldName) != null)
                            .collect(Collectors.toMap(
                                    fieldName -> fieldName,
                                    fieldName -> struct.get(fieldName)
                            ));

                    if(payload != null) {
                        this.customerRatingsService.replicateData(payload, operation);
                        log.info("Updated Data: {} with Operation: {}", payload, operation.name());
                    }
                }
            }
        }catch(Exception ex){
            log.error("An error occured in cdc handleChangeEvent.");
            log.error(ex.getMessage());
        }
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

}