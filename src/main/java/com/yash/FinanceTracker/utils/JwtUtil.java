package com.yash.FinanceTracker.utils;

import com.yash.FinanceTracker.constants.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    public String generateToken(DefaultOAuth2User user)
    {
        Map<String, Object> claims= new HashMap<>();
        claims.put("userName", user.getAttribute("name"));
        claims.put("userEmail", user.getAttribute("email"));

        return createToken(claims, user.getAttribute("name").toString());
    }


    private String createToken(Map<String,Object> claims, String subject)
    {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*60))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token)
    {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(AppConstants.SECRET_KEY.getBytes());
    }

    public String extractUserName(String token)
    {
        return extractAllClaims(token).getSubject();
    }

    private Date extractExpiration(String token)
    {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
