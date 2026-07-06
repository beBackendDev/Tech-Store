package net.myapplication.myapp.user.service.impl;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.myapplication.myapp.user.service.JWTService;

//khong dung, thay the bang AuthserImpl
@Service
public class JWTServiceImpl implements JWTService {

    private static final long ACCESS_TOKEN_EXPIRATION
            = 1000 * 60 * 15;

    private static final long REFRESH_TOKEN_EXPIRATION
            = 1000L * 60 * 60 * 24 * 7;

    @Override
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSiginKey() {
        byte[] keyBytes = Decoders.BASE64.decode("your-secret-key");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateRefreshToken'");
    }
}
