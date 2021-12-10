package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "application_item")
@Data
@NoArgsConstructor
public class ApplicationItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    @JsonBackReference("application")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "price", nullable = false)
    private double price;
}