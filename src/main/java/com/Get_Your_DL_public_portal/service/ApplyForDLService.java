package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.dto.DL_UserDets;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import org.springframework.http.ResponseEntity;

public interface ApplyForDLService {
    ResponseEntity<?> saveDLDets(DL_UserDets dlUserDets);
}
