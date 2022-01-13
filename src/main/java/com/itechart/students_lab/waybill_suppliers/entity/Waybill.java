package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "waybill")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Waybill extends BaseEntity {
    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "waybill")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference("waybill")
    private Set<Application> applications;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creating_user_id", referencedColumnName = "id", nullable = false)
    private Employee creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "updating_user_id", referencedColumnName = "id", nullable = false)
    private Employee lastUpdater;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Employee driver;

    @Column(name = "registration_date_time", nullable = false)
    private LocalDateTime registrationTime;

    @Column(name = "last_update_date_time", nullable = false)
    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private WaybillState state;

    public Waybill(Warehouse warehouse, Employee creator, WaybillState state) {
        this.warehouse = warehouse;
        this.creator = creator;
        this.state = state;
    }

    public Waybill(Long id, Warehouse warehouse) {
        super(id);
        this.warehouse = warehouse;
    }
}
