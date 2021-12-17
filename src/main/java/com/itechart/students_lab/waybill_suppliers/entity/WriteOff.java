package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "write_off")
@Data
@NoArgsConstructor
public class WriteOff extends BaseEntity {
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "writeOff", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference("writeOff")
    private Set<WriteOffItem> writeOffItems;

    @ManyToOne
    @JoinColumn(name = "creating_user_id")
    private Employee creatingUser;

    @ManyToOne

    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
}