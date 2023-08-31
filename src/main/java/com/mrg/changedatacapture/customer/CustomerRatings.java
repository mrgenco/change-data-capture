package com.mrg.changedatacapture.customer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerRatings {
    @Id
    private Integer id;

    @Column(name = "customerno")
    private String customerNo;

    @Column(name = "ratingscore")
    private String ratingScore;

    @Column(name = "analysistype")
    private Short analysisType;
}
