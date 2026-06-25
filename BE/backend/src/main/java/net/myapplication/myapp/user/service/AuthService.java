package net.myapplication.myapp.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDTO<?>> signUp(SignUpRequestDto signUpRequestDto) 
                    throws UserAlreadyExistsException, RoleNotFoundException;

    ResponseEntity<ApiResponseDTO<?>> signIn(SignInRequestDto signInRequestDto) ;   
    
    ResponseEntity<ApiResponseDTO<?>> refreshToken(String refreshToken, HttpServletResponse response);

    ResponseEntity<ApiResponseDTO<?>> logout(String refreshToken, HttpServletResponse response);
}

