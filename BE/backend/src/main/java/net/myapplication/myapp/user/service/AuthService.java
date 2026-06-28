package net.myapplication.myapp.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignInResponseDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDTO<?>> signUp(SignUpRequestDto signUpRequestDto) 
                    throws UserAlreadyExistsException, RoleNotFoundException;

    ResponseEntity<ApiResponseDTO<?>> signIn(SignInRequestDto signInRequestDto) ;   

    //cookie
    SignInResponseDto signInWithCookie(SignInRequestDto signInRequestDto) ;   
    
    
    SignInResponseDto  refreshAccessToken(String refreshToken);

    ResponseEntity<ApiResponseDTO<?>> logout(String refreshToken);
}

