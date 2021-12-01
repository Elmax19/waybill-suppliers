package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @Embedded
    private ContactInformation contactInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference("customer")
    private Customer customer;

}
