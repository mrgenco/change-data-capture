package com.mrg.changedatacapture.customer;

import io.debezium.data.Envelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerRatingsService {

    private final CustomerRatingsRepository customerRepository;

    public void replicateData(Map<String, Object> customerData, Envelope.Operation operation) {
        try {
            final CustomerRatings customerRating = new CustomerRatings();

            // TODO : this is temporary, use ModelMapper instead
            for(Map.Entry<String, Object> customerRatingData : customerData.entrySet()){
                if(customerRatingData.getKey().equals("Id"))
                    customerRating.setId((Integer) customerRatingData.getValue());
                else if(customerRatingData.getKey().equals("AnalysisType"))
                    customerRating.setAnalysisType((Short) customerRatingData.getValue());
                else if(customerRatingData.getKey().equals("RatingScore"))
                    customerRating.setRatingScore((String) customerRatingData.getValue());
                else if(customerRatingData.getKey().equals("CustomerNo"))
                    customerRating.setCustomerNo((String) customerRatingData.getValue());
            }

            if (Envelope.Operation.DELETE.equals(operation)) {
                customerRepository.deleteById(customerRating.getId());
            } else {
                customerRepository.save(customerRating);
            }
        } catch (Exception e) {
            log.error("Error converting customerData to CustomerRatings: {}", e.getMessage());
            e.printStackTrace();
        }

    }
}
