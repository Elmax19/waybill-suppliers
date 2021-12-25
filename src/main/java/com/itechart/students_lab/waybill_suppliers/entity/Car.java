package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "car")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Car extends BaseEntity {
    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "last_address_id", referencedColumnName = "id", nullable = false)
    private Address lastAddress;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "available_capacity", nullable = false)
    private Integer availableCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "c_status", nullable = false)
    private CarStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    public Car(Customer customer) {
        this.customer = customer;
    }

    public Car(Integer totalCapacity, Customer customer) {
        this.totalCapacity = totalCapacity;
        this.customer = customer;
    }
}
