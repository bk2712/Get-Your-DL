package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.UserDetail;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface MyProfileService {
    ResponseEntity<?> fetchMyDetails();

    ResponseEntity<?> updateMyDets(UserDetail userDetail, UUID userId);
}
