package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "write_off_item")
@Data
@NoArgsConstructor
public class WriteOffItem extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "reason", nullable = false)
    private String reason;
}