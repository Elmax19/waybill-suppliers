package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "warehouse")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Warehouse extends BaseEntity {
    @Column(name = "w_name")
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "type")
    private String type;

    @Column(name = "total_capacity")
    private int totalCapacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
}
