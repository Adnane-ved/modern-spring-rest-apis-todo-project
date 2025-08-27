package com.spring.todos.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Value("${spring.jwt.expiration}")
    private long JWTEXPIRATION;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, (claims)-> claims.getSubject());
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsFunction){
        final Claims claims = getClaimsFromToken(token);
        return claimsFunction.apply(claims);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // used to provide the secret key to parseSignedClaims
                .build()
                .parseSignedClaims(token) // parse (split) the jwt into components (header, payload, signature) after validating jwt
                .getPayload(); // used to extract the claims
    }


    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + JWTEXPIRATION))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSigningKey()) // create the signature by using secret key and Signature Algorithm hmac
                .compact(); // converts the build token into String

    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = userDetails.getUsername();
        return (username.equals(extractUsername(token))) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpriationDate(token).before(new Date());
    }

    private Date extractExpriationDate(String token){
        return extractClaim(token,(claims)-> claims.getExpiration());
    }
}
