package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.ApplyForLicense;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplyForLicenseRepo extends JpaRepository<LicenseDetail, UUID> {

    List<LicenseDetail> getByCreatedBy(String userId);
}
