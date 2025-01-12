package com.ocr.p3back.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtAuthFilter jwtAuthFilter;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public SpringSecurityConfig(UserDetailsService userDetailsService,
                              JwtAuthFilter jwtAuthFilter,
                              PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthFilter = jwtAuthFilter;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Configures the security filter chain for the application.
   *
   * @param httpSecurity The HttpSecurity object to be configured.
   * @return A configured SecurityFilterChain object.
   * @throws Exception If there is an error during configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, ("/api/auth/login")).permitAll()
            .requestMatchers(HttpMethod.POST, ("/api/auth/register")).permitAll()
            .requestMatchers(HttpMethod.GET, ("/swagger-ui/**")).permitAll()
            .requestMatchers(HttpMethod.GET, ("/v3/api-docs/**")).permitAll()
            .requestMatchers("/pictures/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(authenticationProvider())
        .build();
  }

  /**
   * Creates and returns an AuthenticationManager bean.
   *
   * @param config The AuthenticationConfiguration object used to configure the AuthenticationManager.
   * @return The configured AuthenticationManager.
   * @throws Exception If there is an error creating the AuthenticationManager.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  private AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return authenticationProvider;
  }
}