package net.myapplication.myapp.user.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import net.myapplication.myapp.user.service.UserSer;

@Component
public class AuthSerImpl implements AuthService {

    @Value("${myapp.jwtRefreshTokenExpiration}")
    private int jwtRefreshTokenExpiration;

    private final UserSer userService;

    private final RefreshTokenSer refreshTokenService;

    private final PasswordEncoder passwordEncoder;

    private final RoleFactory roleFactory;

    private final UserRepo userRepo;

    private final RefreshTokenRepo refreshTokenRepo;

    //login
    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    @Autowired
    public AuthSerImpl(PasswordEncoder passwordEncoder,
            RoleFactory roleFactory,
            UserSer userService,
            AuthenticationManager authenticationManager,
            JWTUtils jwtUtils,
            UserRepo userRepo,
            RefreshTokenRepo refreshTokenRepo, RefreshTokenSer refreshTokenService
    ) {
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.roleFactory = roleFactory;
        this.userService = userService;
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<?>> signUp(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        // (1) Kiem tra tinh dung dan
        if (userService.existByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided email already exists. Try sign in or provide another email.");
        }
        if (userService.existByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided username already exists. Try sign in or provide another username.");
        }
        // (2) Kiem tra ton tai trong DB, neu chua thi create
        User user = createUser(signUpRequestDto);
        //(3) Luu thong tin DB
        userService.save(user);
        //(4) Tra ve response
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.builder()
                        .status(String.valueOf(ResponseStatus.SUCCESS))
                        .message("User account has been successfully created!")
                        .build()
        );
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
        // (1): Xac thuc thong tin dang nhap bang cach tao mot doi tuong 
        // UsernamePasswordAuthenticationToken từ email và mật khẩu. 
        // Sau đó, sử dụng authenticationManager để xác thực thông tin này và trả về đối tượng Authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword())
        );
        // (2): Đặt đối tượng Authentication vào SecurityContext để quản lý bảo mật cho session hiện tại.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // (3): Tạo một JSON Web Token (JWT) từ thông tin xác thực.
        String jwt = jwtUtils.generateJwtToken(authentication);

        // (4): Lấy thông tin chi tiết của người dùng từ đối tượng Authentication.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Khởi tạo RefreshToken
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        Long id = userDetails.getId();
        User dbUser
                = userRepo
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("User not found with id: " + id)
                        );

        refreshTokenService.saveRefreshToken(
                dbUser,
                refreshToken
        );

        // (5): Lấy danh sách các roles của người dùng và chuyển đổi từ set sang list.
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        // (6): Khởi tạo đối tượng SignInResponseDto để trả về kết quả cho client.
        SignInResponseDto sighInResponseDto = SignInResponseDto.builder()
                .token(jwt)
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
                        .build()
        );
    }
}
