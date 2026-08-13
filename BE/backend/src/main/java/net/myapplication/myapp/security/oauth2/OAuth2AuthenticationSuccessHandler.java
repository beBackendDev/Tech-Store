package net.myapplication.myapp.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.user.dto.SignInResponseDto;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.service.AuthService;

@Component
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;

    // @Value("${myapp.frontend-url}")
    // private String frontendUrl;

    @Value("${myapp.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${myapp.jwtRefreshTokenExpiration:300000}")
    private long refreshTokenExpiration;

    public OAuth2AuthenticationSuccessHandler(
            AuthService authService) {

        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // B1: lấy OAuth2 authentication
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        // B2:Lấy Google user
        OAuth2User oauth2User = oauthToken.getPrincipal();
        // B3:Lấy Google Email

        String email = oauth2User.getAttribute("email");

        System.out.println(
                "Google login success: " + email);
        // B4. Tìm hoặc tạo User trong DB

        User user;

try {

    user = authService.findOrCreateGoogleUser(
            oauth2User
    );

} catch (RoleNotFoundException e) {

    response.sendRedirect(
            "http://localhost:5173/login?oauth2Error=true"
    );

    return;
}

        // B5. Tạo Access Token + Refresh Token

        SignInResponseDto result = authService.createTokensForUser(
                user);
        // B6. Tạo HttpOnly Refresh Cookie

        ResponseCookie refreshCookie = ResponseCookie
                .from(
                        "refreshToken",
                        result.getRefreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(
                        refreshTokenExpiration / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString());

        /*
         * 7. Không đưa Access Token
         * vào URL
         */
        response.sendRedirect(
                "http://localhost:5173/oauth2/success");
    }
}