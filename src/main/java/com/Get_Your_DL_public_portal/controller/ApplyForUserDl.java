package com.Get_Your_DL_public_portal.controller;

import com.Get_Your_DL_public_portal.dto.ApplyForDlPayload;
import com.Get_Your_DL_public_portal.dto.DL_UserDets;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.service.ApplyForDLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ApplyForUserDl {

    @Autowired
    ApplyForDLService applyForDLService;

    private static final Logger LOG = LoggerFactory.getLogger(ApplyForUserDl.class);

    @GetMapping("/ping")
    public ResponseEntity<?> getPing(){
        LOG.info("fill details api is executed");
        return ResponseEntity.ok("the ping is here");
    }


    @PostMapping(value = "/fill-details",     // the second segment here
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> uploadDets(@RequestBody ApplyForDlPayload dlUserDets){
//        return ResponseEntity.ok("My bad");
        LOG.info("fill details api is executed: {}", dlUserDets);
        return applyForDLService.saveDLDets(dlUserDets);
    }

    @GetMapping("/licenseDetails/get/{userId}")
    public List<LicenseDetail> getLicenseDets(@PathVariable UUID userId){
        return applyForDLService.fetchLicenseDetailsForUser(userId);
    }
}
