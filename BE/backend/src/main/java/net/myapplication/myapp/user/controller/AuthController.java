package net.myapplication.myapp.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;
import net.myapplication.myapp.user.service.AuthService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {
    
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponseDTO<?>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        return authService.signUp(signUpRequestDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseDTO<?>> signInUser(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return authService.signIn(signInRequestDto);
    }
}

