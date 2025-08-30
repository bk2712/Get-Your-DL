package com.Get_Your_DL_public_portal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "otp_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "otp", nullable = false)
    private Integer otp;
    @Column(name = "otp_expiration_time")
    private Timestamp expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserDetail usersData;
}
