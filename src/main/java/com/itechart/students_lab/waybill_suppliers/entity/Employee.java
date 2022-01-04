package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @Embedded
    private ContactInformation contactInformation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference("customer")
    private Customer customer;

    public Employee(Long id) {
        super(id);
    }
}
