package com.itechart.students_lab.waybill_suppliers.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application extends BaseEntity {
    @Column(name = "number", nullable = false)
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @OneToOne(cascade = CascadeType.MERGE)
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

    @ManyToOne
    @JoinColumn(name = "waybill_id", referencedColumnName = "id")
    private Waybill waybill;

    @Column(name = "is_outgoing", nullable = false)
    private boolean outgoing;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference("application")
    private Set<ApplicationItem> items;

    public Application(Warehouse warehouse, ApplicationStatus status, boolean outgoing) {
        this.warehouse = warehouse;
        this.status = status;
        this.outgoing = outgoing;
    }
}

