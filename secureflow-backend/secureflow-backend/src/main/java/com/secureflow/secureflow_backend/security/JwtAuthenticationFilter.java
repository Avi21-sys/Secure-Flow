package com.secureflow.secureflow_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        /*
            Get Authorization header
        */
        final String authHeader =
                request.getHeader("Authorization");


        String jwt = null;
        String email = null;


        /*
            Check if header contains Bearer token
        */
        if(authHeader != null &&
                authHeader.startsWith("Bearer ")){

            jwt = authHeader.substring(7);

            email = jwtService.extractUsername(jwt);

        }


        /*
            Authenticate user if:

            1. Token exists
            2. User is not already authenticated
        */
        if(email != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null){


            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(email);



            /*
                Validate JWT
            */
            if(jwtService.isTokenValid(
                    jwt,
                    userDetails
            )){


                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );


                /*
                    Store authentication in Security Context
                */
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);

            }

        }


        /*
            Continue request
        */
        filterChain.doFilter(
                request,
                response
        );

    }
}
