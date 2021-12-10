package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "is_outgoing", nullable = false)
    private boolean outgoing;

    @OneToMany(mappedBy = "application")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference("application")
    private Set<ApplicationItem> items;
}

