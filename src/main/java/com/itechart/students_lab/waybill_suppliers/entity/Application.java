package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
public class Application extends BaseEntity {
    @Column(name = "number", nullable = false)
    private int number;

    @OneToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @OneToOne
    @JoinColumn(name = "destination_address_id", referencedColumnName = "id")
    private Address destinationAddress;

    @Column(name = "registration_date_time", nullable = false)
    private LocalDateTime registrationDateTime;

    @Column(name = "last_update_date_time", nullable = false)
    private LocalDateTime lastUpdateDateTime;

    @OneToOne
    @JoinColumn(name = "creating_user_id", referencedColumnName = "id")
    private Employee creatingUser;

    @OneToOne
    @JoinColumn(name = "updating_user_id", referencedColumnName = "id")
    private Employee updatingUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private ApplicationStatus status;

    @ManyToMany
    @JoinTable(name = "application_item",
        joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items;
}

