package com.Get_Your_DL_public_portal.filter;

import com.Get_Your_DL_public_portal.service.JwtServiceImpl;
import com.Get_Your_DL_public_portal.service.JwtTokenService;
import com.Get_Your_DL_public_portal.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// this class is basically responsible for authentication of users
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtServiceImpl jwtService;

    @Autowired
    UserServiceImpl userDetsService;

    @Autowired
    JwtTokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Header => " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenService.isTokenBlacklisted(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalidated");
                return;
            }
            String username = jwtService.extractUsername(token); // returns sub (email)
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetsService.loadUserByUsername(username);

                if (jwtService.isValid(token, userDetails)) {
                    System.out.println("✅ Token is valid for user: " + username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else System.out.println("❌ Token is NOT valid for user: " + username);
            }
        }
        filterChain.doFilter(request, response); // without this line your request will never reach the controller
    }

}
