package net.myapplication.myapp.user.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.enumpack.ResponseStatus;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.mail.entity.VerificationToken;
import net.myapplication.myapp.mail.repository.VerificationTokenRepo;
import net.myapplication.myapp.mail.service.MailSer;
import net.myapplication.myapp.mail.service.VerificationTokenSer;
import net.myapplication.myapp.password.dto.ResetPasswordRequest;
import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.password.service.ResetPasswordSer;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignInResponseDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;
import net.myapplication.myapp.user.entity.Role;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;
import net.myapplication.myapp.user.refreshtoken.repository.RefreshTokenRepo;
import net.myapplication.myapp.user.refreshtoken.service.RefreshTokenSer;
import net.myapplication.myapp.user.repository.UserRepo;
import net.myapplication.myapp.user.service.AuthService;
import net.myapplication.myapp.user.service.JWTUtils;
import net.myapplication.myapp.user.service.RoleFactory;
import net.myapplication.myapp.user.service.UserDetailsServiceImpl;
import net.myapplication.myapp.user.service.UserSer;

@Component
public class AuthSerImpl implements AuthService {

        private final UserDetailsServiceImpl userDetailsServiceImpl;

        private final UserSer userService;
        private final UserRepo userRepo;

        private final RefreshTokenSer refreshTokenService;

        private final MailSer mailService;
        private final ResetPasswordSer resetPasswordService;

        private final VerificationTokenSer verificationTokenService;
        private final VerificationTokenRepo verificationTokenRepo;

        private final PasswordEncoder passwordEncoder;

        private final RoleFactory roleFactory;

        // login
        private final AuthenticationManager authenticationManager;

        private final RefreshTokenRepo refreshTokenRepo;

        private final JWTUtils jwtUtils;

        @Autowired
        public AuthSerImpl(PasswordEncoder passwordEncoder,
                        RoleFactory roleFactory,
                        UserSer userService,
                        AuthenticationManager authenticationManager,
                        JWTUtils jwtUtils,
                        UserRepo userRepo,
                        RefreshTokenRepo refreshTokenRepo,
                        RefreshTokenSer refreshTokenService,
                        UserDetailsServiceImpl userDetailsServiceImpl, VerificationTokenSer verificationTokenService,
                        MailSer mailService, VerificationTokenRepo verificationTokenRepo,
                        ResetPasswordSer resetPasswordService) {
                this.refreshTokenService = refreshTokenService;
                this.mailService = mailService;
                this.resetPasswordService = resetPasswordService;
                this.verificationTokenService = verificationTokenService;
                this.verificationTokenRepo = verificationTokenRepo;
                this.passwordEncoder = passwordEncoder;
                this.roleFactory = roleFactory;
                this.userService = userService;
                this.userRepo = userRepo;
                this.refreshTokenRepo = refreshTokenRepo;
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
                this.userDetailsServiceImpl = userDetailsServiceImpl;
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> signUp(SignUpRequestDto signUpRequestDto)
                        throws UserAlreadyExistsException, RoleNotFoundException {
                // (1) Kiem tra tinh dung dan
                if (userService.existByEmail(signUpRequestDto.getEmail())) {
                        throw new UserAlreadyExistsException(
                                        "Registration Failed: Provided email already exists. Try sign in or provide another email.");
                }
                if (userService.existByUsername(signUpRequestDto.getUsername())) {
                        throw new UserAlreadyExistsException(
                                        "Registration Failed: Provided username already exists. Try sign in or provide another username.");
                }
                // (2) Kiem tra ton tai trong DB, neu chua thi create
                User user = createUser(signUpRequestDto);
                // (3) Luu thong tin DB
                user.setEnabled(false);
                userService.save(user);
                // (4) Thuc hien verification email
                VerificationToken token = verificationTokenService
                                .createVerificationToken(user);

                mailService.sendVerificationEmail(
                                user,
                                token.getToken());
                // (5) Tra ve response
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("User account has been successfully created!")
                                                .build());
        }

        private User createUser(SignUpRequestDto signUpRequestDto) throws RoleNotFoundException {
                return User.builder()
                                .email(signUpRequestDto.getEmail())
                                .username(signUpRequestDto.getUsername())
                                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                                .enabled(true)
                                .roles(determineRoles(signUpRequestDto.getRoles()))
                                .build();
        }

        private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
                Set<Role> roles = new HashSet<>();

                if (strRoles == null) {
                        roles.add(roleFactory.getInstance("user"));
                } else {
                        for (String role : strRoles) {
                                roles.add(roleFactory.getInstance(role));
                        }
                }
                return roles;
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> signIn(SignInRequestDto signInRequestDto) {
                // (0) Xác thực thông tin mail đăng nhập
                User user = userRepo
                                .findByEmail(signInRequestDto.getEmail())
                                .orElseThrow(
                                                () -> new RuntimeException("User not found with email: "
                                                                + signInRequestDto.getEmail()));
                if (!user.isEnabled()) {
                        throw new RuntimeException(
                                        "User is not verified. Please verify your email before signing in.");
                }
                // (1): Xac thuc thong tin dang nhap bang cach tao mot doi tuong
                // UsernamePasswordAuthenticationToken từ email và mật khẩu.
                // Sau đó, sử dụng authenticationManager để xác thực thông tin này và trả về đối
                // tượng Authentication
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                signInRequestDto.getEmail(),
                                                signInRequestDto.getPassword()));
                // (2): Đặt đối tượng Authentication vào SecurityContext để quản lý bảo mật cho
                // session hiện tại.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // (3): Lấy thông tin chi tiết của người dùng từ đối tượng Authentication.
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                // (4): Tạo một JSON Web Token (JWT) từ thông tin xác thực.
                String accessToken = jwtUtils.generateJwtToken(userDetails);
                // Khởi tạo RefreshToken
                String refreshToken = jwtUtils.generateRefreshToken(userDetails);
                Long id = userDetails.getId();
                User dbUser = userRepo
                                .findById(id)
                                .orElseThrow(
                                                () -> new RuntimeException("User not found with id: " + id));

                refreshTokenService.saveRefreshToken(
                                dbUser,
                                refreshToken);

                // (5): Lấy danh sách các roles của người dùng và chuyển đổi từ set sang list.
                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());
                // (6): Khởi tạo đối tượng SignInResponseDto để trả về kết quả cho client.
                SignInResponseDto sighInResponseDto = SignInResponseDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken) // tam thoi
                                .id(userDetails.getId())
                                .username(userDetails.getUsername())
                                .email(userDetails.getEmail())
                                .roles(roles)
                                .build();
                // (7): Trả về response chứa thông tin đăng nhập thành công.
                return ResponseEntity.ok().body(
                                ApiResponseDTO.builder()
                                                .status(String.valueOf(ResponseStatus.SUCCESS))
                                                .message("Sign in successful!")
                                                .response(sighInResponseDto)
                                                .build());
        }

        @Override
        public SignInResponseDto signInWithCookie(SignInRequestDto signInRequestDto) {
                // (1): Xac thuc thong tin dang nhap bang cach tao mot doi tuong
                // UsernamePasswordAuthenticationToken từ email và mật khẩu.
                // Sau đó, sử dụng authenticationManager để xác thực thông tin này và trả về đối
                // tượng Authentication
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                signInRequestDto.getEmail(),
                                                signInRequestDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String accessToken = jwtUtils.generateJwtToken(userDetails);
                String refreshToken = jwtUtils.generateRefreshToken(userDetails);
                Long id = userDetails.getId();
                User dbUser = userRepo
                                .findById(id)
                                .orElseThrow(
                                                () -> new RuntimeException("User not found with id: " + id));

                refreshTokenService.saveRefreshToken(
                                dbUser,
                                refreshToken);
                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());
                // (6): Khởi tạo đối tượng SignInResponseDto để trả về kết quả cho client.
                SignInResponseDto sighInResponseDto = SignInResponseDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken) // tam thoi
                                .id(userDetails.getId())
                                .username(userDetails.getUsername())
                                .email(userDetails.getEmail())
                                .roles(roles)
                                .build();
                return sighInResponseDto;

        }

        @Override
        // public ResponseEntity<ApiResponseDTO<?>> refreshToken(String refreshToken,
        // HttpServletResponse response) { //khi su dung cookie
        public SignInResponseDto refreshAccessToken(String refreshToken) {
                RefreshToken tokenEntity = refreshTokenService
                                .verifyToken(refreshToken);
                String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(username);
                String newAccessToken = jwtUtils.generateJwtToken(userDetails);

                // revoked old refreshToken
                tokenEntity.setRevoked(true);
                refreshTokenRepo.save(
                                tokenEntity);

                String newRefreshToken = jwtUtils.generateRefreshToken(
                                userDetails);
                refreshTokenService.saveRefreshToken(
                                tokenEntity.getUser(),
                                newRefreshToken);
                // ghi vao cookie

                // tra ve SignInResponseDTO
                List<String> roles = userDetails.getAuthorities()
                                .stream()
                                .map(
                                                authority -> authority.getAuthority())
                                .toList();

                // SignInResponseDto dto
                // = SignInResponseDto.builder()
                // .accessToken(newAccessToken)
                // .refreshToken(newRefreshToken)
                // .id(userDetails.getId())
                // .username(userDetails.getUsername())
                // .email(userDetails.getEmail())
                // .roles(roles)
                // .build();
                // chi can return access va refresh Token
                return SignInResponseDto.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken) // ← controller cần cái này để set cookie
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> logout(String refreshToken) {
                // revoke refresh token
                refreshTokenService
                                .revokeToken(refreshToken);

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status("SUCCESS")
                                                .message("Logout successful")
                                                .build());

        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> resendVerification(String email) {
                User user = userRepo
                                .findByEmail(email)
                                .orElseThrow(
                                                () -> new RuntimeException("User not found with email: " + email));

                if (user.isEnabled()) {
                        throw new RuntimeException("User is already verified.");
                }

                VerificationToken token = verificationTokenService
                                .resendVerificationToken(user);
                mailService.sendVerificationEmail(
                                user,
                                token.getToken());

                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status("SUCCESS")
                                                .message("Verification email sent.")
                                                .build());
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> verifyEmail(String token) {
                VerificationToken verificationToken = verificationTokenService
                                .verifyToken(token);

                User user = verificationToken.getUser();
                user.setEnabled(true);

                userService.save(user);
                verificationTokenRepo.delete(verificationToken);
                return ResponseEntity.ok(
                                ApiResponseDTO.builder()
                                                .status("SUCCESS")
                                                .message("Email verified successfully")
                                                .build());
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> forgotPassword(String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(
                                                () -> new RuntimeException(
                                                                "Email does not exist."));

                if (!user.isEnabled()) {

                        throw new RuntimeException(
                                        "Account has not been verified.");

                }
                ResetPasswordToken token = resetPasswordService
                                .createResetPasswordToken(
                                                user);

                mailService.sendResetPasswordEmail(
                                user,
                                token.getToken());

                return ResponseEntity.ok(

                                ApiResponseDTO.builder()
                                                .status(
                                                                String.valueOf(
                                                                                ResponseStatus.SUCCESS))
                                                .message(
                                                                "Password reset email has been sent.")
                                                .build()

                );
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> verifyResetToken(String token) {
                resetPasswordService.verifyToken(
                                token);

                return ResponseEntity.ok(

                                ApiResponseDTO.builder()
                                                .status(
                                                                String.valueOf(
                                                                                ResponseStatus.SUCCESS))
                                                .message(
                                                                "Reset password token is valid.")
                                                .build()

                );
        }

        @Override
        public ResponseEntity<ApiResponseDTO<?>> resetPassword(ResetPasswordRequest request) {
                if (!request.getNewPassword().equals(request.getConfirmPassword())) {

                        throw new RuntimeException(
                                        "Password confirmation does not match.");

                }

                ResetPasswordToken token = resetPasswordService.verifyToken(
                                request.getToken());

                User user = token.getUser();

                user.setPassword(
                                passwordEncoder.encode(
                                                request.getNewPassword()));

                userRepo.save(user);

                // revoke toàn bộ refresh token
                refreshTokenService.revokeAllUserTokens(
                                user);

                // đánh dấu token đã dùng
                token.setUsed(true);

                resetPasswordService.save(token);

                return ResponseEntity.ok(

                                ApiResponseDTO.builder()
                                                .status(
                                                                String.valueOf(ResponseStatus.SUCCESS))
                                                .message(
                                                                "Password has been reset successfully.")
                                                .build()

                );
        }

}
