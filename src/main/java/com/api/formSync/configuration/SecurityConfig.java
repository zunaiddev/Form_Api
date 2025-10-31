package com.api.formSync.configuration;

import com.api.formSync.Filter.ApiKeyAuthFilter;
import com.api.formSync.Filter.JwtAuthFilter;
import com.api.formSync.Filter.RateLimitFilter;
import com.api.formSync.Filter.VerificationFilter;
import com.api.formSync.Service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder encoder;
    private final ApiKeyAuthFilter apiKeyAuthFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final VerificationFilter verificationFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final RateLimitFilter rateLimitFilter;
    @Value("${BASE_URL}")
    private String BASE_URL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**", "/error", "/public/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(apiKeyAuthFilter, RateLimitFilter.class)
                .addFilterAfter(jwtAuthFilter, ApiKeyAuthFilter.class)
                .addFilterAfter(verificationFilter, JwtAuthFilter.class)
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration publicCorsConfig = new CorsConfiguration();
        publicCorsConfig.setAllowedOrigins(List.of("*"));
        publicCorsConfig.setAllowedMethods(List.of("POST"));
        publicCorsConfig.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/public/**", publicCorsConfig);

        CorsConfiguration privateCorsConfig = new CorsConfiguration();
        privateCorsConfig.setAllowedOrigins(List.of(
                "http://localhost:3000",
                BASE_URL
        ));
        privateCorsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        privateCorsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        privateCorsConfig.setAllowCredentials(true);

        source.registerCorsConfiguration("/auth/**", privateCorsConfig);
        source.registerCorsConfiguration("/users/**", privateCorsConfig);
        source.registerCorsConfiguration("/verify/**", privateCorsConfig);
//        source.registerCorsConfiguration("/admin/**", privateCorsConfig);

        return source;
    }
}
