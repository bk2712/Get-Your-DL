package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.EmailVerification;
import com.Get_Your_DL_public_portal.repository.EmailVerificationRepo;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronSchedulers {

    @Autowired
    EmailVerificationRepo emvRepo;

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void removeExpiredEntriesOfEmail(){
        Log.info("Let's remove expired resources.....");
        emvRepo.deleteExpiredTokens();
    }
}
