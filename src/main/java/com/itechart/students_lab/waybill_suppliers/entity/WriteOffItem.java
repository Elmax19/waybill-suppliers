package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private WriteOffReason reason;

    @ManyToOne
    @JoinColumn(name = "write_off_id", nullable = false)
    private WriteOff writeOff;
}