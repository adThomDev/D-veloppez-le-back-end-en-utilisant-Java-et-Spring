package com.ocr.p3back.config;

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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired
  private UserDetailsService userDetailsService; // Service pour gérer les détails des utilisateurs

  @Autowired
  private JwtAuthFilter jwtAuthFilter; // Filtre d'authentification basé sur JWT

  @Bean
  public PasswordEncoder getEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable()) //à commenter pour activer la defense contre le CSRF (NB : mettre aussi ds les form POST une clé csrf (après balise form))
//        .formLogin(form -> form
//            //l'url de login, et les url en cas d'identification réussie ou échouée :
//            .loginPage("/login").defaultSuccessUrl("/index").failureUrl("/login?error=true")
//            .permitAll()
//        )
//        .logout(logout-> logout
//            .logoutUrl("/logout") //l'url de déconnexion
//            .permitAll()
//        )
        .authorizeHttpRequests(auth -> auth
            //nb: "/webjars/**" pr que le webjar puisse installer les dépendances (eg bootstrap. voir ds fragments (méthode diff de cdn))
//            .requestMatchers("api/auth/login","/login","/inscription","/webjars/**","/css/**", "/script/**", "/img/**").permitAll()
            //pour permettre à un visiteur de s'identifier en utilisant le formulaire :
            .requestMatchers(HttpMethod.POST,("/api/auth/login")).permitAll()
            //accès des différents rôles utilisateurs :
//            .requestMatchers("/index").hasAnyAuthority("Superviseur","Professeur","Administrateur")
//            .requestMatchers("/api/**","/etablissement/**","/professeur/**","/session/**").hasAuthority("Administrateur")
//            .requestMatchers("/superviseur/**").hasAnyAuthority("Superviseur","Administrateur")
//            .requestMatchers("/jure/**").hasAnyAuthority("Professeur","Administrateur")
            .anyRequest().authenticated()
//                .anyRequest().permitAll()
        )
        .sessionManagement((httpSecurity1)-> httpSecurity1.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        //pour l'identification par JWT (à utiliser avec loginfetch.js), si e.g. version mobile (donc avec API) :
//        .authenticationProvider(authenticationProvider())
//        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager(); // Configure le gestionnaire d'authentification
  }

  //pour l'authentification par JWT :
  private AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(getEncoder());
    return authenticationProvider; // Configure le fournisseur d'authentification pour l'application
  }

}

//  @Bean
//  public WebSecurityCustomizer webSecurityCustomizer() {
//    return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
//    // Permet d'accéder à la console H2 sans authentification (utile pour le développement)
//  }
