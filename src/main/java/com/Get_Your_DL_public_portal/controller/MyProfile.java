package com.Get_Your_DL_public_portal.controller;


import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.service.MyProfileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/my-profile")
public class MyProfile {

    @Autowired
    MyProfileService mypfservice;

    @GetMapping()
    public ResponseEntity<?> getUserPfDets(){
        return mypfservice.fetchMyDetails();
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserDetail userDetail, @PathVariable UUID userId){
        return mypfservice.updateMyDets(userDetail, userId);
    }
}
