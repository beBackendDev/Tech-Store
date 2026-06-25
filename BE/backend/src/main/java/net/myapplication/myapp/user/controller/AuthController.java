package net.myapplication.myapp.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;
import net.myapplication.myapp.user.refreshtoken.dto.RefreshTokenRequest;
import net.myapplication.myapp.user.service.AuthService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.myapplication.myapp.enumpack.ResponseStatus;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<ApiResponseDTO<?>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        return authService.signUp(signUpRequestDto);
    }

    //login / signin
    @PostMapping("/auth/sign-in")
    public ResponseEntity<ApiResponseDTO<?>> signInUser(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return authService.signIn(signInRequestDto);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponseDTO<?>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        //TODO: process POST request

        return authService.refreshToken(request.getRefreshToken(), null);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponseDTO<?>> logout(
            @RequestBody RefreshTokenRequest request
    ) {
        //TODO: process POST request

        return authService.logout(request.getRefreshToken(), null);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public String roleUser() {
        return "USER!!!";
    }

    @GetMapping("/dashboard/admin")
    //hasAuthority thi DB la ADMIN | hasRole thi DB phai ROLE_ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO<?>> AdminDashboard() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.builder()
                        .status(String.valueOf(ResponseStatus.SUCCESS))
                        .message("Admin dashboard!")
                        .build());
    }

}
