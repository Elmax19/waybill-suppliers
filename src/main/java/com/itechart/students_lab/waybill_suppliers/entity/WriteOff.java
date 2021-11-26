package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "write_off")
@Data
@NoArgsConstructor
public class WriteOff extends BaseEntity{
    @Column(name = "date_time", nullable = false)
    private String fullName;

    @JoinTable
    @OneToMany(mappedBy = "write_off", cascade = CascadeType.ALL)
    private List<WriteOffItem> writeOffItems;
}