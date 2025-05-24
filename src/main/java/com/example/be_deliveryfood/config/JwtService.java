package com.example.be_deliveryfood.config;

import com.example.be_deliveryfood.entity.User;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(User user) throws Exception {
        JWSSigner signer = new MACSigner(secret.getBytes());
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expiration * 1000))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public String getUsernameFromToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        if (!signedJWT.verify(new MACVerifier(secret.getBytes()))) {
            throw new Exception("Invalid token signature");
        }
        return signedJWT.getJWTClaimsSet().getSubject();
    }
}