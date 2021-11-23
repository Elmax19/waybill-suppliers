package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_active")
    private ActiveStatus activeStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Employee> employees;
}
