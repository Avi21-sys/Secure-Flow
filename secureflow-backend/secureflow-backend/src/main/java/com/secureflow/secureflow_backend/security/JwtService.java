package com.secureflow.secureflow_backend.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /*
       Generate JWT Token
     */
    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(System.currentTimeMillis() + expiration)
                )
                .signWith(getSigningKey())
                .compact();

    }


    /*
       Extract username/email from token
     */
    public String extractUsername(String token){

        return extractClaim(
                token,
                Claims::getSubject
        );

    }


    /*
       Validate token
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ){

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);

    }


    /*
       Check expiration
     */
    private boolean isTokenExpired(String token){

        return extractExpiration(token)
                .before(new Date());

    }


    /*
       Extract expiration date
     */
    private Date extractExpiration(String token){

        return extractClaim(
                token,
                Claims::getExpiration
        );

    }


    /*
       Generic claim extractor
     */
    private <T> T extractClaim(
            String token,
            Function<Claims,T> resolver
    ){

        final Claims claims = extractAllClaims(token);

        return resolver.apply(claims);

    }


    /*
       Extract all claims
     */
    private Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }


    /*
       Convert secret string into signing key
     */
    private SecretKey getSigningKey(){

        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );

    }
}
