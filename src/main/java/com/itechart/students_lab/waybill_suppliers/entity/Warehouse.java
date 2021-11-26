package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
public class Warehouse extends BaseEntity{

}
