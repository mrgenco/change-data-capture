package com.mrg.changedatacapture.customer;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    private Long id;

    @Column(name = "CustomerNo")
    private String customerNo;

    @Column(name = "RatingScore")
    private String ratingScore;

    @Column(name = "AnalysisType")
    private Integer analysisType;
}
