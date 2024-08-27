package com.dr.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private static String secretKey = "";

    // Manually taken secretKey is not secure, so I declared constructor to generate secretKey automatically
    public JWTService(){
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk= keyGenerator.generateKey();
            secretKey= Arrays.toString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 100))   //6 min
                .and()
                .signWith(getKey())
                .compact();
    }

    //This method called inside this class only
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //This method called inside JWTFilter class
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //This method called inside JWTFilter class
    public boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername= extractUsername(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //This method called inside this class only
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //This method called inside this class only
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //This method called inside this class only
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //This method called inside this class only
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
}