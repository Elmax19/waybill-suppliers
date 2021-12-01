package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

