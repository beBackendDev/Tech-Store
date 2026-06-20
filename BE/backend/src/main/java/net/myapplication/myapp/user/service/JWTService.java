package net.myapplication.myapp.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

//khong dung, thay the bang Authser
@Service
public interface JWTService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);

}
