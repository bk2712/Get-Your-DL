package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.AuthenticationResponse;
import com.Get_Your_DL_public_portal.entity.ResetPassword;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import com.Get_Your_DL_public_portal.repository.ResetPasswordRepo;
import com.Get_Your_DL_public_portal.repository.UserDetailsRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class RegisterLoginServiceImpl implements RegisterLoginService {

    @Autowired
    UserDetailsRepo regLogRepo;

    @Autowired
    ResetPasswordRepo resetPasswordRepo;

    @Autowired
    SendMailService sendEmail;

    @Autowired
    UserAuthenticationImpl authentication;

    @Autowired
    PasswordEncoder passwordEncoder;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^[6-9]\\d{9}$";

    private static final Logger LOG = LoggerFactory.getLogger(RegisterLoginServiceImpl.class);

    @Override
    public ResponseEntity<?> registerUser(UserDetail userDets) {
        // correct email format, phone number, password = confirmPassword
        if(userDets.getEmail().matches(EMAIL_REGEX) && (userDets.getPhone() == null || userDets.getPhone().matches(PHONE_REGEX)) && userDets.getPassword().matches(userDets.getConfirmPassword())){
            String hashedPassword = encoder.encode(userDets.getPassword());
            userDets.setPassword(hashedPassword);
            UserDetail savedUser= regLogRepo.save(userDets);
            UUID generatedId = savedUser.getId();
            AuthenticationResponse response = authentication.register(userDets, generatedId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(400).body("Something went wrong!");
    }

    @Override
    public ResponseEntity<?> signin(UserDetail userDets) {
        LOG.info("Login intitated");
        if(regLogRepo.findIfUserExists(userDets.getEmail())){
            UserDetail usd= regLogRepo.findByEmail(userDets.getEmail());
            LOG.info("Please find the user by email: {}", usd);
            String email= userDets.getEmail();
            String password= userDets.getPassword();

//            UserDetail usd= regLogRepo.findByEmailAndPassword(email, password);
            LOG.info("User login is initiated : {}", usd);
            if(usd != null && passwordEncoder.matches(password, usd.getPassword())) {
                AuthenticationResponse response = authentication.authenticate(usd);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(400).body("User does not exists!");
    }

    @Override
    public ResponseEntity<?> sendOtp(String email) {
        UserDetail usd= regLogRepo.findByEmail(email);
        LOG.info("User exists while sharing otp: {}", usd);
        if(usd != null){
            // do not share otp with user until previous otp expires
            ResetPassword existingOtp = resetPasswordRepo.findTopByUsersDataOrderByExpirationTimeDesc(usd);
            if (existingOtp != null && existingOtp.getExpirationTime().after(new Timestamp(System.currentTimeMillis()))) {
                return ResponseEntity.status(429).body("OTP already sent. Please check your email.");
            }

            Integer otp= generateOtp();
            ResetPassword resetpswdObj= new ResetPassword();
            resetpswdObj.setOtp(otp);
            resetpswdObj.setExpirationTime(new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000));
            resetpswdObj.setUsersData(usd);
            sendEmail.sendEmailToUsers("otp for setting password", "Hi! "+usd.getFirstname()+" "+usd.getLastname()+" here is the otp for setting password: "+otp,email);
            resetPasswordRepo.save(resetpswdObj);
            return ResponseEntity.ok("Otp is shared!");
        }
        return ResponseEntity.status(400).body("Invalid email!");
    }

    @Override
    public ResponseEntity<?> verifyOtp(String email, ResetPassword otp) {
//        UserDetails usd= regLogRepo.findByEmail(email);
        Integer otpNum= otp.getOtp();
        ResetPassword otpDets= resetPasswordRepo.findByOtpAndEmail(otpNum, email);
        if(otpDets != null){
            Date expTime= otpDets.getExpirationTime();
            if(expTime.after(new Date(System.currentTimeMillis())))
                return ResponseEntity.ok("Otp is accepted!");
            return ResponseEntity.status(400).body("Otp is expired!");
        }
        return ResponseEntity.status(400).body("Invalid otp");
    }

    @Override
    @Transactional
    public ResponseEntity<?> resetPassword(String email, UserDetail details) {
        LOG.info("Password : {} ConfirmPassword: {}", details.getConfirmPassword(), details.getPassword());
        if(!details.getConfirmPassword().equals(details.getPassword())) return ResponseEntity.status(400).body("Password and confirm password doesn't match");
        String newPswrd= encoder.encode(details.getPassword());
        Integer updatedRows= regLogRepo.updateUserPswrd(newPswrd, email);
        if (updatedRows != 0) return ResponseEntity.ok("Password is updated successfully!");
        return ResponseEntity.status(400).body("Password is updated successfully!");
    }


    private Integer generateOtp() {
        Random random= new Random();
        return random.nextInt(1000, 99999);
    }

}
