package com.Get_Your_DL_public_portal.entity;

import com.Get_Your_DL_public_portal.config.RawJsonToString;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name= "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "staff_id")
    private Integer staffId;
    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "lastname", nullable = false)
    private String lastname;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip")
    private Integer zip;
    @Enumerated(value= EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password", nullable = false)
    private String password;
    @Transient
    private String confirmPassword;
    private Timestamp createdAt;
    private String createdBy;
    private Timestamp updated_at;
    private String updatedBy;
    @Column(name = "dob")
    private String dob;
    @Column(name = "spouse_name")
    private String spouseName;
    @Column(name = "age")
    private Integer age;
    @Column(name = "spouseAge")
    private Integer spouseAge;
    @Column(name = "child_dets", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonRawValue
    @JsonDeserialize(using = RawJsonToString.class)
    private String childDetails;
    @Column(name = "crime_records", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonRawValue
    @JsonDeserialize(using = RawJsonToString.class)
    private String crimeRecords;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
