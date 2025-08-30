package com.Get_Your_DL_public_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String fileName;
    @Column(name = "mimeType")
    private String fileMimeType;
    @Column(name = "type")
    private String fileType;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "path")
    private String filePath;

}
