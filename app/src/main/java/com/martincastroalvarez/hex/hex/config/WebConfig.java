package com.martincastroalvarez.hex.hex.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import org.springframework.security.config.Customizer;
import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.martincastroalvarez.hex.hex.domain.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.logging.Logger;
import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.MDC;
import jakarta.servlet.*;
import java.util.UUID;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private JwtFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://hex.martincastroalvarez.com", "https://hex.martincastroalvarez.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "HexFileMetadata"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<MdcLogEnhancerFilter> loggingFilter() {
        FilterRegistrationBean<MdcLogEnhancerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MdcLogEnhancerFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (email) -> userRepository.get(email).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
        authenticationStrategy.setPasswordEncoder(passwordEncoder());
        authenticationStrategy.setUserDetailsService(userDetailsService());
        return authenticationStrategy;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http
            .csrf(csrfConfig -> csrfConfig.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authReqConfig -> {
                authReqConfig.requestMatchers(HttpMethod.POST, "/auth/signup").permitAll();
                authReqConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                authReqConfig.requestMatchers(HttpMethod.POST, "/auth/reset").permitAll();

                authReqConfig.requestMatchers(HttpMethod.GET, "/users").hasAnyRole(User.Role.ADMIN.name(), User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.PUT, "/users/[0-9]+").hasAnyRole(User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]+/status").hasAnyRole(User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/users/[0-9]+/status").hasAnyRole(User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/schedule").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/users/[0-9]*/schedule/[0-9]*").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/pto").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/users/[0-9]*/pto/[0-9]*").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/meetings").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/messages").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/sales").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/purchases").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/users/[0-9]*/work").hasAnyRole(User.Role.MANAGER.name());

                authReqConfig.requestMatchers(HttpMethod.GET, "/managers").hasAnyRole(User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/managers/[0-9]*").hasAnyRole(User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/managers/[0-9]*").hasAnyRole(User.Role.ADMIN.name());

                authReqConfig.requestMatchers(HttpMethod.GET, "/providers").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/providers/[0-9]*").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/providers/[0-9]*").hasAnyRole(User.Role.MANAGER.name());

                authReqConfig.requestMatchers(HttpMethod.GET, "/salesmen").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/salesmen/[0-9]*").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/salesmen/[0-9]*").hasAnyRole(User.Role.MANAGER.name());

                authReqConfig.requestMatchers(HttpMethod.POST, "/meetings").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/meetings/[0-9]+").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.PUT, "/meetings/[0-9]+").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/meetings/[0-9]+/files").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/meetings/[0-9]+/files/[0-9]+").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/meetings/[0-9]+/users/[0-9]+").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/meetings/[0-9]+/users/[0-9]+").hasAnyRole(User.Role.MANAGER.name(), User.Role.ADMIN.name());

                authReqConfig.requestMatchers(HttpMethod.POST, "/messages").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/messages/[0-9]+").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.PUT, "/messages/[0-9]+").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/messages/[0-9]+").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/messages/[0-9]+/users/[0-9]+").hasAnyRole(User.Role.MANAGER.name());
                authReqConfig.requestMatchers(HttpMethod.DELETE, "/messages/[0-9]+/users/[0-9]+").hasAnyRole(User.Role.MANAGER.name());

                authReqConfig.requestMatchers(HttpMethod.GET, "/products/[0-9]+/purchases").hasAnyRole(User.Role.MANAGER.name(), User.Role.PROVIDER.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/products/[0-9]+/purchases").hasAnyRole(User.Role.PROVIDER.name());
                authReqConfig.requestMatchers(HttpMethod.GET, "/products/[0-9]+/sales").hasAnyRole(User.Role.MANAGER.name(), User.Role.SALESMAN.name());
                authReqConfig.requestMatchers(HttpMethod.POST, "/products/[0-9]+/sales").hasAnyRole(User.Role.SALESMAN.name());

                authReqConfig.requestMatchers(HttpMethod.POST, "/").hasAnyRole(User.Role.USER.name());
                authReqConfig.anyRequest().authenticated();
            })
            .build();
        return filterChain;
    }
}

class MdcLogEnhancerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String requestUUID = UUID.randomUUID().toString();
            MDC.put("RequestId", requestUUID);

            String username = getUsername(request);
            if (username != null) {
                MDC.put("UserEmail", username);
            }

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {}

    private String getUsername(ServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated())
            return authentication.getName();
        return null;
    }
}

@Component
class JwtFilter extends OncePerRequestFilter {
    protected static final Logger logger = Logger.getLogger(JwtFilter.class.getName());

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            logger.info(String.format("Filtering request: %s %s", request.getRequestURI(), header));
            if(!StringUtils.hasText(header) || !header.startsWith("Bearer ")){
                logger.info(String.format("Anonymous request: %s %s", request.getRequestURI(), header));
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.split(" ")[1];
            User user = authenticationService.parseToken(token);
            if (!user.getIsActive())
                throw new UserDisabledException();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info(String.format("Authenticated request: %s %s (%s)", request.getRequestURI(), user.getEmail(), user.getRole().toString()));
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.info(String.format("Expired token: %s", request.getRequestURI()));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            logger.info(String.format("Unauthorized request: %s", request.getRequestURI()));
            logger.info(String.format("Error: %s %s", e.toString(), e.getMessage()));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
