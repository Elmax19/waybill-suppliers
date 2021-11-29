package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "car")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Car extends BaseEntity {
    @Column(name = "car_number")
    private String carNumber;

    @OneToOne
    @JoinColumn(name = "last_address_id", referencedColumnName = "id")
    private Address lastAddress;

    @Column(name = "total_capacity")
    private int totalCapacity;

    @Column(name = "available_capacity")
    private int availableCapacity;
}
