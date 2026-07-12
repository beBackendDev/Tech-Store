package net.myapplication.myapp.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.enumpack.ResponseStatus;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.password.dto.ForgotPasswordRequest;
import net.myapplication.myapp.password.dto.ResetPasswordRequest;
import net.myapplication.myapp.password.dto.VerifyResetPasswordRequest;
import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignInResponseDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;
import net.myapplication.myapp.user.refreshtoken.dto.RefreshTokenRequest;
import net.myapplication.myapp.user.service.AuthService;
import net.myapplication.myapp.user.service.AuthTokenFilter;

import java.util.List;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class AuthController {
        private static final Logger logger = Logger.getLogger(AuthController.class.getName());
        private final AuthService authService;

        @Value("${myapp.cookie.secure:false}")
        private boolean cookieSecure;// true khi production (HTTPS) false khi local(HTTP)

        @Value("${myapp.jwtRefreshTokenExpiration:300000}")
        private long refreshTokenExpiration; // milliseconds

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @PostMapping("/auth/sign-up")
        public ResponseEntity<ApiResponseDTO<?>> registerUser(
                        @RequestBody @Valid SignUpRequestDto signUpRequestDto)
                        throws UserAlreadyExistsException, RoleNotFoundException {

                authService.signUp(signUpRequestDto);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(
                                                ApiResponseDTO.builder()
                                                                .status(ResponseStatus.SUCCESS.name())
                                                                .message("User account has been successfully created!")
                                                                .build());
        }

        @GetMapping("/auth/verify-email")
        public ResponseEntity<ApiResponseDTO<?>> verifyEmail(
                        @RequestParam String token) {

                authService.verifyEmail(token);

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Email verified successfully.")
                                                .build());
        }

        @PostMapping("/auth/resend-verification")
        public ResponseEntity<ApiResponseDTO<?>> resendVerification(
                        @RequestParam String email) {

                authService.resendVerification(email);

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Verification email sent.")
                                                .build());
        }

        // login / signin
        @PostMapping("/auth/sign-in")
        public ResponseEntity<ApiResponseDTO<?>> signInUser(
                        @RequestBody @Valid SignInRequestDto signInRequestDto) {
                //
                SignInResponseDto signInResult = authService.signInWithCookie(signInRequestDto);

                //
                ResponseCookie refreshCookie = buildRefreshCookie(
                                signInResult.getRefreshToken(),
                                refreshTokenExpiration / 1000 // convert ms -> seconds
                );
                return ResponseEntity.ok()
                                // Set-Cookie header — browser tự lưu, FE không đọc được bằng JS
                                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                                .body(ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Đăng nhập thành công")
                                                // Chỉ trả accessToken trong body, KHÔNG trả refreshToken
                                                .response(signInResult.getAccessToken())
                                                .build());
        }

        @PostMapping("/auth/cookie/refresh")
        public ResponseEntity<ApiResponseDTO<?>> refreshTokenWithCookie(
                        HttpServletRequest request) {
                String oldRefreshToken = AuthTokenFilter.extractRefreshTokenFromCookie(request);
                if (oldRefreshToken == null) {
                        System.out.println("refreshtoken null");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(ApiResponseDTO.builder()
                                                        .status(String.valueOf(ResponseStatus.FAIL))
                                                        .message("Invalid RefresToken")
                                                        .build());
                }
                // authService.refreshToken() validate token trong DB và trả về accessToken mới
                SignInResponseDto result = authService.refreshAccessToken(oldRefreshToken);
                ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                                .httpOnly(true)
                                .secure(false)
                                .path("/api/auth")
                                .maxAge(7 * 24 * 3600)
                                .sameSite("Lax")
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(ApiResponseDTO.builder()
                                                .status("SUCCESS").message("Token refreshed")
                                                .response(result.getAccessToken()).build());
        }

        @PostMapping("/auth/logout")
        public ResponseEntity<ApiResponseDTO<?>> logout(
                        HttpServletRequest request) {
                String refreshToken = AuthTokenFilter.extractRefreshTokenFromCookie(request);
                if (refreshToken != null) {
                        // Xóa refresh token khỏi DB
                        authService.logout(refreshToken);
                }
                // Clear cookie bằng cách set maxAge=0
                ResponseCookie clearCookie = buildRefreshCookie("", 0);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                                .body(ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Đăng xuất thành công")
                                                .build());
        }

        @PostMapping("/auth/forgot-password")
        public ResponseEntity<ApiResponseDTO<?>> forgotPassword(
                        @RequestBody @Valid ForgotPasswordRequest request,
                        HttpServletRequest httpRequest) {

                logger.info("URI: " + httpRequest.getRequestURI());

                authService.forgotPassword(request.getEmail());

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Password reset email has been sent.")
                                                .build());
        }

        @PostMapping("/auth/verify-reset-password")
        public ResponseEntity<ApiResponseDTO<?>> verifyResetPassword(
                        @RequestBody VerifyResetPasswordRequest request) {

                authService.verifyResetToken(request.getToken());

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Reset password token is valid.")
                                                .build());
        }

        @PostMapping("/auth/reset-password")
        public ResponseEntity<ApiResponseDTO<?>> resetPassword(
                        @RequestBody @Valid ResetPasswordRequest request) {

                authService.resetPassword(request);

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Password has been reset successfully.")
                                                .build());
        }

        @GetMapping("/user")
        @PreAuthorize("hasAuthority('USER')")
        public String roleUser() {
                return "USER!!!";
        }

        @GetMapping("/dashboard/admin")
        // hasAuthority thi DB la ADMIN | hasRole thi DB phai ROLE_ADMIN
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<ApiResponseDTO<?>> AdminDashboard() {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Admin dashboard!")
                                                .build());
        }

        // Helper: tạo ResponseCookie với đầy đủ thuộc tính bảo mật
        private ResponseCookie buildRefreshCookie(String value, long maxAgeSeconds) {
                return ResponseCookie.from("refreshToken", value)
                                .httpOnly(true) // JS không đọc được → chống XSS
                                .secure(cookieSecure) // true = chỉ gửi qua HTTPS (bật khi production)
                                .path("/api/auth") // Cookie chỉ gửi đến /api/auth/* — không gửi mọi request
                                .maxAge(maxAgeSeconds) // 0 = xóa cookie ngay lập tức (dùng cho logout)
                                .sameSite("Lax") // Lax: an toàn với CSRF, vẫn hoạt động với link điều hướng
                                .build();
        }
}
