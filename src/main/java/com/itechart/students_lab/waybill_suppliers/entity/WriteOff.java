package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "write_off")
@Data
@NoArgsConstructor
public class WriteOff extends BaseEntity{
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "writeOff", cascade = CascadeType.ALL)
    private List<WriteOffItem> writeOffItems;
}