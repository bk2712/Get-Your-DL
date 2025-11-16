package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.EmailVerification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepo extends JpaRepository<EmailVerification, Integer> {
    public EmailVerification findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailVerification e WHERE e.expireAt < CURRENT_TIMESTAMP")
    int deleteExpiredTokens();
}
