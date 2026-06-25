package net.myapplication.myapp.user.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.info("URI: {}", request.getRequestURI());
            logger.info("JWT: {}", jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                logger.info("JWT VALID");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
logger.info("EMAIL FROM TOKEN: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("USER FOUND: {}", userDetails.getUsername());
                UsernamePasswordAuthenticationToken authentication
                
                        = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.info(
    "Authenticated = {}",
    authentication.isAuthenticated()
);                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("AUTHENTICATION SET");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
logger.info(
    "AUTH AFTER SET = {}",
    SecurityContextHolder.getContext().getAuthentication()
);
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
