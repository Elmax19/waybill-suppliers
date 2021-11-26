package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "retailer")
@Data
@NoArgsConstructor
public class Retailer extends BaseEntity{
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, columnDefinition = "ACTIVE")
    private ActiveStatus activeStatus;

    @JoinTable
    @OneToMany
    private List<Warehouse> warehouses;
}
