package com.Get_Your_DL_public_portal.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenService {
    private Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
