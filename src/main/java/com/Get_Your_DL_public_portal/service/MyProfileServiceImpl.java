package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.repository.DocumentRepo;
import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MyProfileServiceImpl implements MyProfileService{

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    UserDetailsRepo userDetailsRepo;

    @Autowired
    HttpServletRequest request;

    @Autowired
    DocumentRepo documentRepo;


    @Override
    public ResponseEntity<?> fetchMyDetails() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID fetchedUserId= fetchLoggedInUserIdFromToken(token);

        Optional<UserDetail> user = userDetailsRepo.findById(fetchedUserId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Integer profilePicId= documentRepo.getProfilePicId(fetchedUserId);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = mapper.convertValue(user.get(), new TypeReference<Map<String, Object>>() {});

        response.put("profileImageId", profilePicId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateMyDets(UserDetail userDetail, UUID userId) {
        userDetailsRepo.updateUserProfileWithOptionalFields(userDetail.getFirstname(), userDetail.getLastname(), userDetail.getAddress(), userDetail.getCity(), userDetail.getState(), userDetail.getZip(), userDetail.getEmail(), userDetail.getDob(), userDetail.getSpouseName(), userDetail.getAge(), userDetail.getSpouseAge(), userDetail.getChildDetails(), userDetail.getCrimeRecords(), userId);
        return ResponseEntity.ok("Details has been updated successfully!");
    }

    public UUID fetchLoggedInUserIdFromToken(String token) {
        try {
            Claims claims = jwtService.extractAllClaims(token);
            return UUID.fromString(claims.get("userId", String.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract userId from token", e);
        }
    }
}
