package com.Get_Your_DL_public_portal.controller;

import com.Get_Your_DL_public_portal.dto.DL_UserDets;
import com.Get_Your_DL_public_portal.entity.ResetPassword;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.service.ApplyForDLService;
import com.Get_Your_DL_public_portal.service.RegisterLoginService;
import com.Get_Your_DL_public_portal.service.UserAuthenticationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserRegisterLogin {

    @Autowired
    RegisterLoginService regLogService;

    @Autowired
    ApplyForDLService applyForDLService;

    private static final Logger LOG = LoggerFactory.getLogger(UserRegisterLogin.class);

    @PostMapping("/register")
    public ResponseEntity<?> userReg(@RequestBody UserDetail userDets){
        return regLogService.registerUser(userDets);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserDetail userDets){
        LOG.info("Login api called");
        return regLogService.signin(userDets);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> sendOtp(@RequestParam String email){
        LOG.info("Email shared: {}", email);
        return regLogService.sendOtp(email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email, @RequestBody ResetPassword otp){
        return regLogService.verifyOtp(email, otp);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<?> setNewPassword(@RequestParam String email, @RequestBody UserDetail details){
        return regLogService.resetPassword(email, details);
    }


}
