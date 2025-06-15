package com.openclassrooms.ycyw_back.configs;

import com.openclassrooms.ycyw_back.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
// OncePerRequestFilter -> run on every request.
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // Look for a bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Passes through our filter
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Retrieves the JWT.
            final String jwt = authHeader.substring(7);
            // Retrieves the right username from the token.
            final String userName = jwtService.extractUsername(jwt);

            logger.info("userName: {}", userName);

            // Retrieves the current authentication.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // If username valid and not already authenticated :
            if (userName != null && authentication == null) {
                // Gets user details.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Creates the authentication token with the user details.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // User details.
                            null, // Credentials (not needed anymore).
                            userDetails.getAuthorities() // User roles (no roles in this context).
                    );

                    // Adds other information like the IP address or session ID (not used here in favour of JWT) to the token.
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Sets the security context.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Allow the request.
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}