package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    UserDetailsRepo usersRepo;

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOG.info("UserServiceImpl class is also initiated now ..... and params username is: {}", email);
        return (UserDetails) usersRepo.findByEmail(email);
    }
}
