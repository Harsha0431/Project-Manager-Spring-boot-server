package com.manager.config;

import com.ApiResponse.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;


public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String[] publicApis = {"/", "/api/auth/register", "/api/auth/login"};

    private static final LinkedHashSet<String> publicApisList = new LinkedHashSet<>(
            Arrays.asList(publicApis)
    );

    private final TokenService tokenService; // Service to handle token operations

    public TokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // System.out.println("CAME TO doFilterInternal with token " + token);
        String requestUrl = request.getRequestURI();
        if(publicApisList.contains(requestUrl)){
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(null, null, null);
            filterChain.doFilter(request, response);
            return;
        }
        String token = tokenService.getTokenFromRequest(request);
        if (token != null && tokenService.isTokenValid(token)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            ApiResponse<String> res = new ApiResponse<String>(-1, "Please login to continue.", null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ow.writeValueAsString(res));
        }
    }
}

