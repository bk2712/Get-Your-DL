package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.ResetPassword;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import org.springframework.http.ResponseEntity;

public interface RegisterLoginService {
    public ResponseEntity<?> registerUser(UserDetail userDets);

    public ResponseEntity<?> signin(UserDetail userDets);

    public ResponseEntity<?> sendOtp(String email);

    public ResponseEntity<?> verifyOtp(String email, ResetPassword otp);

    ResponseEntity<?> resetPassword(String email, UserDetail details);
}
