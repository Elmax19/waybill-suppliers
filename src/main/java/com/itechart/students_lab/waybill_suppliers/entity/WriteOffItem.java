package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "write_off_item")
@Data
@NoArgsConstructor
public class WriteOffItem extends BaseEntity {
    @OneToOne
    private Item item;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private WriteOffReason reason;

    @ManyToOne
    @JoinColumn(name = "write_off_id", nullable = false)
    @JsonBackReference("writeOff")
    private WriteOff writeOff;
}