package com.mrg.changedatacapture.customer;


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
    private String customerNo;
    private String ratingScore;
    private Integer analysisType;
}
