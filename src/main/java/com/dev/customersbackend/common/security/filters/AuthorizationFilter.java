package com.dev.customersbackend.common.security.filters;

import com.dev.customersbackend.common.security.exceptions.SecurityException;
import com.dev.customersbackend.common.security.helpers.token.TokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsService service;
    private final TokenHelper helper;

    public AuthorizationFilter(UserDetailsService service, TokenHelper helper) {
        this.service = service;
        this.helper = helper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/auth")) {
            try {
                String jwtToken = request.getHeader("Authorization");
                if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                    throw new SecurityException("Token nulo");
                }
                String subject = helper.validateToken(jwtToken.replace("Bearer ", ""));
                UserDetails user = service.loadUserByUsername(subject);
                UsernamePasswordAuthenticationToken internalToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(internalToken);
            } catch (Exception exception) {
                throw new SecurityException(
                        "Token inválido, expirado ou o usuário não possui permissão para acessar este recurso");
            }
        }
        filterChain.doFilter(request, response);
    }
}
