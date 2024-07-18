//package com.namuk.gadget.security.config;
//
//import com.namuk.gadget.security.filters.JwtAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class JwtConfig {
//
//    private final AuthenticationConfiguration configuration;
//
//    public JwtConfig(AuthenticationConfiguration configuration) {
//        this.configuration = configuration;
//    }
//
//    @Bean
//    public SecurityFilterChain JwtFilterChain(@Qualifier("JwtAuthenticationManager") AuthenticationManager authenticationManager, HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/", "/join").permitAll()
//                        .anyRequest().authenticated()
//                );
//                return http.build();
//    }
//
//    @Bean(name = "JwtPasswordEncoder")
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(@Qualifier("JwtAuthenticationManager") AuthenticationManager authenticationManager) {
//        return new JwtAuthenticationFilter(authenticationManager);
//    }
//
//    @Bean(name = "JwtAuthenticationManager")
//    public AuthenticationManager authenticationManager() throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//}
