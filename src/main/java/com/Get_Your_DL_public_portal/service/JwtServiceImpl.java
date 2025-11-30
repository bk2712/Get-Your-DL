package com.Get_Your_DL_public_portal.service;

import com.Get_Your_DL_public_portal.entity.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl {
    private final String SECRET_KEY= "c82edea70473ebde4f4d05ff231c226e0b119e79d2f6623e2aa692acaa785de0";


    // extract claims
    Claims extractAllClaims(String token){
        //claim is payload
        // this function will basically extract payload
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        // this method is used to extract specific field from payload
        Claims claims= extractAllClaims(token);
        System.out.println("Claims=> "+claims);
        return resolver.apply(claims);
    }

    public String extractUsername(String token){
        System.out.println("extractClaim => "+extractClaim(token, Claims::getSubject));
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        boolean usernameMatches = username.equals(user.getUsername());
        boolean notExpired = !isTokenExpired(token);
        System.out.println("username => "+username);
        System.out.println("Token username: [" + user + "]");
        System.out.println("DB username: [" + user.getUsername() + "]");
        System.out.println("Username matches: " + usernameMatches);
        System.out.println("Token expired: " + !notExpired);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // generate token

    public String generateToken(UserDetail users){
        //build jwt token
        //mtlb decode krne pr payload me kya kya dikhana chate ho

        String name= users.getFirstname() + " "+ users.getLastname();
        String token= Jwts
                .builder()
                .claim("role", users.getRole())
                .claim("name", name)
                .claim("userId", users.getId())
                .subject(users.getEmail())  // this is the email
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    //get getSignInKey
    private SecretKey getSignInKey(){
        byte[] keyBytes= Decoders.BASE64URL.decode(SECRET_KEY);
        System.out.println("KeyBytes => "+keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
