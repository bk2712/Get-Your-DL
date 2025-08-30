package com.Get_Your_DL_public_portal.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "license_request_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyForLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "applied_on")
    private Timestamp appliedOn;
    @Column(name = "approved_on")
    private Timestamp approvedOn;
    @OneToOne
    @JoinColumn(name = "license_details_id", nullable = false, referencedColumnName = "id")
    private LicenseDetail licenseDetail;
}
