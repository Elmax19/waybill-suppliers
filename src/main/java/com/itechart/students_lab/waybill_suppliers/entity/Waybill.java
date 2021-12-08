package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "waybill")
@Entity
@Data
@NoArgsConstructor
public class Waybill extends BaseEntity{

    @Column(name = "number")
    private Integer number;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source_address_id", referencedColumnName = "id")
    private Address sourceAddress;

    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @OneToOne
    @JoinColumn(name = "creating_user_id", referencedColumnName = "id")
    private Employee creator;

    @OneToOne
    @JoinColumn(name = "updating_user_id", referencedColumnName = "id")
    private Employee lastUpdater;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Employee driver;

    @Column(name = "registration_date_time")
    private LocalDateTime registrationTime;

    @Column(name = "last_update_date_time")
    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private WaybillState waybillState;
}
