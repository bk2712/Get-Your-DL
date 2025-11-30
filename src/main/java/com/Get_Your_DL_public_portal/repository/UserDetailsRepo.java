package com.Get_Your_DL_public_portal.repository;

import com.Get_Your_DL_public_portal.entity.UserDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetail, UUID> {

    public UserDetail findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserDetail u WHERE u.email = :email")
    public boolean findIfUserExists(@Param("email") String email);

    public UserDetail findByEmailAndPassword(String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE UserDetail u SET u.password = :password WHERE u.email = :email")
    Integer updateUserPswrd(@Param("password") String newPswrd, @Param("email") String email);


    @Modifying
    @Transactional
    @Query(value = """
    UPDATE users SET 
        firstname    = COALESCE(:firstname, firstname),
        lastname     = COALESCE(:lastname, lastname),
        address      = COALESCE(:address, address),
        city         = COALESCE(:city, city),
        state        = COALESCE(:state, state),
        zip          = COALESCE(:zip, zip),
        email        = COALESCE(:email, email),
        dob          = COALESCE(:dob, dob),
        spouse_name  = COALESCE(:spouseName, spouse_name),
        age          = COALESCE(:age, age),
        spouse_age   = COALESCE(:spouseAge, spouse_age),
        child_dets   = COALESCE(CAST(:childDetails AS jsonb), child_dets),
        crime_records = COALESCE(CAST(:crimeRecords AS jsonb), crime_records)
    WHERE id = :id
""", nativeQuery = true)
    int updateUserProfileWithOptionalFields(
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            @Param("address") String address,
            @Param("city") String city,
            @Param("state") String state,
            @Param("zip") Integer zip,
            @Param("email") String email,
            @Param("dob") String dob,
            @Param("spouseName") String spouseName,
            @Param("age") Integer age,
            @Param("spouseAge") Integer spouseAge,
            @Param("childDetails") String childDetails,     // Accepts raw JSON string
            @Param("crimeRecords") String crimeRecords,     // Accepts raw JSON string
            @Param("id") UUID id
    );


}
