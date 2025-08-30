package com.spring.todos.config;


import com.spring.todos.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // ensure that the great filter "JwtAuthenticationFilter" run every single time we have an http request


    private JwtService jwtService;

    private UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(@Lazy UserDetailsService userDetailsService,JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // in header the key "Authorization" has the value jwt, Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ") ) { // means there is no jwt
            filterChain.doFilter(request, response); // pass request to the next filter, if not called it will not reach the controller
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);


       /* SecurityContextHolder is Spring Security’s holder for the current authentication info.
        getContext().getAuthentication() returns the current user’s authentication object.
        If it is null, no authentication exists yet, so we can proceed to authenticate using the JWT.*/

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if(jwtService.validateToken(jwt, userDetails)) {

                 /* UsernamePasswordAuthenticationToken tells who is the user for this request and store in the SecurityContext
                 why wrapping userDetails in UsernamePasswordAuthenticationToken , why not just using userDetails ?
                 answer => userDetails alone it doesn't give the information if the user is authenticated or not
                 but by wrapping it and storing this wrap in SecurityContext spring knows that this user is authenticated */

                UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials (not needed because JWT is already validated)
                        userDetails.getAuthorities()
                );

                /*
                * WebAuthenticationDetails It’s a Spring Security class that stores additional information about the request.
                * Typically includes:
                * Remote address (request.getRemoteAddr()) → the IP of the client
                * Session ID (request.getSessionId()) → optional, if there’s an HTTP session
                * It does not affect authentication itself, but provides context about the request.
                */

                authToken.setDetails(new WebAuthenticationDetails(request));

                //getContext().setAuthentication() store the current user’s authentication object
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
