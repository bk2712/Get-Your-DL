package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.ResetPassword;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPassword, Integer> {

    public ResetPassword findByOtp(Integer otp);

    @Query("SELECT rp FROM ResetPassword rp WHERE rp.usersData.email = :email AND rp.otp = :otp")
    ResetPassword findByOtpAndEmail(@Param("otp") Integer otp, @Param("email") String email);


    ResetPassword findTopByUsersDataOrderByExpirationTimeDesc(UserDetail usd);
}
