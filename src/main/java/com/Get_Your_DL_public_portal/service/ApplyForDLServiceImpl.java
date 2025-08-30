package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.dto.DL_UserDets;
import com.Get_Your_DL_public_portal.entity.ApplyForLicense;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.repository.ApplyForLicenseRepo;
import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ApplyForDLServiceImpl implements ApplyForDLService{

    @Autowired
    UserDetailsRepo usdRepo;
    @Autowired
    ApplyForLicenseRepo applyRepo;

    @Autowired
    JwtServiceImpl jwtService;

    @Autowired
    HttpServletRequest request;

    private static final Logger LOG = LoggerFactory.getLogger(ApplyForDLServiceImpl.class);

    @Override
    @Transactional
    public ResponseEntity<?> saveDLDets(DL_UserDets dlUserDets) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }
        LOG.info("User id ===> ");
        String token = authHeader.substring(7);
        UUID userId;
        try {
            Claims claims = jwtService.extractAllClaims(token);
            userId = UUID.fromString(claims.get("userId", String.class));
            LOG.info("User id: {}", userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        LOG.info("License details: {} & user details: {}",dlUserDets.getLicenseDetail(), dlUserDets.getUserDetail());
        applyRepo.save(dlUserDets.getLicenseDetail());
        UserDetail userDets= dlUserDets.getUserDetail();
        int updatedCol= usdRepo.updateUserProfileWithOptionalFields(
                userDets.getFirstname(),
                userDets.getLastname(),
                userDets.getAddress(),
                userDets.getCity(),
                userDets.getState(),
                userDets.getZip(),
                userDets.getEmail(),
                userDets.getDob(),
                userDets.getSpouseName(),
                userDets.getAge(),
                userDets.getSpouseAge(),
                userDets.getChildDetails(),
                userDets.getCrimeRecords(),
                userId
        );
        if(updatedCol == 1) return ResponseEntity.ok("Details updates successfully!");
        return ResponseEntity.status(400).body("Something went wrong!");
    }
}
