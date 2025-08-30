package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.Document;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {

    @Query("Select doc.id from Document doc where doc.userId= :userId and fileType= 'Profile_Pic'")
    Integer getProfilePicId(@Param("userId") UUID userId);

    @Query("Select doc.id from Document doc where doc.userId= :userId")
    List<Document> findByUserId(@Param("userId")UUID userId);

    @Query("Select doc from Document doc where doc.userId= :userId and doc.fileType= :fileType")
    Document findByFileType(@Param("fileType") String fileType, @Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("Update Document doc set doc.fileMimeType= :fileMimeType, doc.fileName= :fileName, doc.filePath= :filePath where doc.id= :id")
    Integer update(@Param("fileMimeType") String fileMimeType, @Param("fileName") String fileName, @Param("filePath") String filePath, @Param("id") Integer id);
}
