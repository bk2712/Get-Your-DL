package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.UserDocDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDocsDetails extends JpaRepository<UserDocDetails, UUID> {
}
