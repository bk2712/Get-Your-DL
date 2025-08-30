package com.Get_Your_DL_public_portal.service;


import com.Get_Your_DL_public_portal.entity.AuthenticationResponse;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service

// this class is basically used to generate jwt token by sharing payload formed below:-
public class UserAuthenticationImpl {

    @Autowired
    UserDetailsRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtServiceImpl jwtService;

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationImpl.class);

    public AuthenticationResponse register(UserDetail request, UUID generatedId) {
        UserDetail users= new UserDetail();
        users.setFirstname(request.getFirstname());
        users.setLastname(request.getLastname());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setRole(request.getRole());
        users.setId(generatedId);
        LOG.info("Please check the generated id of user: {}", generatedId);

        String token= jwtService.generateToken(users);
        LOG.info("Please find the token generated for user: {}", token);
        return new AuthenticationResponse(token, "User is registered successfully");

    }

    public AuthenticationResponse authenticate(UserDetail request) {

        LOG.info("User authentication process is initiated........");
        UserDetail users= new UserDetail();
        users.setFirstname(request.getFirstname());
        users.setLastname(request.getLastname());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setRole(request.getRole());
        users.setId(request.getId());
        String token = jwtService.generateToken(users);
        return new AuthenticationResponse(token, "User is now authenticated successfully!");
    }
}
