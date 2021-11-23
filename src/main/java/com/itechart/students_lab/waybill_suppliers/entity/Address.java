package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Data
public class Address extends BaseEntity{

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "address_line_1")
    private String firstAddressLine;

    @Column(name = "address_line_2")
    private String secondAddressLine;

}
