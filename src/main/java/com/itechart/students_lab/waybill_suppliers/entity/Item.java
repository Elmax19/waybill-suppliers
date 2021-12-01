package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
public class Item extends BaseEntity {
    @Column(name = "UPC", nullable = false)
    private Long upc;

    @Column(name = "label", nullable = false)
    private String label;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ItemCategory itemCategory;

    @Column(name = "units", nullable = false)
    private int units;

    @Column(name = "price", nullable = false)
    private double price;
}

