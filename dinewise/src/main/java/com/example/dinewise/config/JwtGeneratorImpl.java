package com.example.dinewise.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.security.Key;

@Service
public class JwtGeneratorImpl implements JwtGeneratorInterface {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${app.jwttoken.message}")
    private String message;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Override
    public Map<String, String> generateToken(Student student) {
        String jwtToken= Jwts.builder()
                .setSubject((student.getUsername()))
                .claim("role", "student")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }


    @Override
    public Map<String, String> generateToken(String subject, String role) {
        String jwtToken = Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }


    // @Override
    // public Map<String, String> generateTokenForMessManager(MessManager messManager) {
    //     String jwtToken = Jwts.builder()
    //             .setSubject(messManager.getStdId())
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date((new Date()).getTime() + expirationTime))
    //             .signWith(key(), SignatureAlgorithm.HS256)
    //             .compact();

    //     Map<String, String> jwtTokenGen = new HashMap<>();
    //     jwtTokenGen.put("token", jwtToken);
    //     jwtTokenGen.put("message", message);
    //     return jwtTokenGen;
    // }

    // public String getUsernameFromToken(String token) {
    //     Claims claims = Jwts.parser()
    //         .setSigningKey(secret) // use your secret
    //         .parseClaimsJws(token)
    //         .getBody();

    //     return claims.getSubject(); // assuming you stored username in 'sub'
    // }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key()) // ✅ Use the same key method used for signing
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // public String getStdIdFromToken(String token) {
    //     Claims claims = Jwts.parser()
    //             .setSigningKey(key()) // ✅ Use the same key method used for signing
    //             .parseClaimsJws(token)
    //             .getBody();

    //     return claims.getSubject(); // Assuming you stored stdId in 'sub'
    // }



    Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}



