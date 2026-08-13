package net.myapplication.myapp.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.exception.UserAlreadyExistsException;
import net.myapplication.myapp.password.dto.ResetPasswordRequest;
import net.myapplication.myapp.user.dto.SignInRequestDto;
import net.myapplication.myapp.user.dto.SignInResponseDto;
import net.myapplication.myapp.user.dto.SignUpRequestDto;
import net.myapplication.myapp.user.entity.User;

@Service
public interface AuthService {
        void signUp(SignUpRequestDto signUpRequestDto)
                        throws UserAlreadyExistsException, RoleNotFoundException;

        void verifyEmail(String token);

        void resendVerification(String email);

        ResponseEntity<ApiResponseDTO<?>> signIn(SignInRequestDto signInRequestDto);

        // password
        void forgotPassword(String email);

        void verifyResetToken(String token);

        void resetPassword(ResetPasswordRequest request);

        // cookie
        public SignInResponseDto createTokensForUser(User user);

        public User findOrCreateGoogleUser(OAuth2User oauth2User)
                        throws RoleNotFoundException;

        SignInResponseDto signInWithCookie(SignInRequestDto signInRequestDto);

        SignInResponseDto refreshAccessToken(String refreshToken);

        ResponseEntity<ApiResponseDTO<?>> logout(String refreshToken);
}
