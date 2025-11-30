package com.Get_Your_DL_public_portal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_docs_detail")
public class UserDocDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "doc_id")
    private int docId;
    @Column(name = "license_id")
    private UUID licenseId;
}
