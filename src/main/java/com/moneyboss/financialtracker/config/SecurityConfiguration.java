package com.moneyboss.financialtracker.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add CORS configuration
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/register").permitAll()
                .requestMatchers("/activation/resend").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/activation/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs.yaml").permitAll()
                .requestMatchers("/coins/**").permitAll()
                .requestMatchers("/items/get-items").hasAnyAuthority("user", "admin")
                .requestMatchers("/items/add-user-item").hasAnyAuthority("user", "admin")
                .requestMatchers("/users/get-current-user").hasAnyAuthority("user", "admin")
                .requestMatchers( "/users/get-current-user-finance").hasAnyAuthority("user", "admin")
                .requestMatchers("/users/update-current-user-finance").hasAnyAuthority("user", "admin")
                .requestMatchers("/debts/**").hasAnyAuthority( "user", "admin")
                .requestMatchers("/expenses/**").hasAnyAuthority("user","admin")
                .requestMatchers("/expense-categories/**").hasAnyAuthority("user", "admin")
                .requestMatchers("/users/get-by-id").hasAnyAuthority( "admin")
                .requestMatchers("/users/delete").hasAuthority("admin")
                .requestMatchers("/users/get-all").hasAuthority("admin")
                .requestMatchers("/items/get-all-items").hasAnyAuthority("admin")
                .requestMatchers("/items/add-item").hasAuthority("admin")
                .requestMatchers("/items/update-item").hasAuthority("admin")
                .requestMatchers("/items/delete-item").hasAuthority("admin")
                .requestMatchers("/admin/scheduler/**").hasAuthority("admin")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

   @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "https://*.ngrok-free.app",
        "https://*.ngrok.io",
        "https://moneyboss-1-env-2.eba-xpbh2wsy.eu-north-1.elasticbeanstalk.com"
    ));
    
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    ));
    
    // TRY BEING EXPLICIT FOR DIAGNOSIS:
    configuration.setAllowedHeaders(Arrays.asList(
        "Authorization", 
        "Content-Type", 
        "Accept", 
        "Origin", 
        "Access-Control-Request-Method", 
        "Access-Control-Request-Headers",
        "X-Requested-With", // Common header
        "ngrok-skip-browser-warning" // If your Flutter app might send this
        // Add any other specific custom headers your Flutter app sends
    ));
    // If the above explicit list works, then "*" wasn't catching something,
    // or a header was being sent that you weren't aware of.
    // If it still fails, the issue is likely with Origin or Methods.
    
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
    }
}