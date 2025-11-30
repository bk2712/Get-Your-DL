package com.Get_Your_DL_public_portal.entity;

import com.Get_Your_DL_public_portal.config.RawJsonToString;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "license_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
//    @ManyToOne
//    @JoinColumn(name = "license_id", referencedColumnName = "id")
//    private License licenseId;
//    @ManyToOne
//    @JoinColumn(name = "license_req_id", nullable = false, referencedColumnName = "id")
//    private ApplyForLicense licenseReqId;
    @Column(name = "vehicle_details", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonRawValue
    @JsonDeserialize(using = RawJsonToString.class)
    private String vehicleDetails;
    @Column(name = "passport_num", nullable = false)
    private String passportNum;
    @Column(name = "resident_num", nullable = false)
    private String residentNum;
    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonRawValue
    @JsonDeserialize(using = RawJsonToString.class)
    private String otherInfo;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "active")
    private Boolean active;
}
