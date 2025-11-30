package com.Get_Your_DL_public_portal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "email_verification")
public class EmailVerification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "token", columnDefinition = "text")
    private String token;
    @Column(name = "expire_at")
    private Timestamp expireAt;
    @Column(name = "is_used", columnDefinition = "boolean default false")
    private boolean isUsed;

}
