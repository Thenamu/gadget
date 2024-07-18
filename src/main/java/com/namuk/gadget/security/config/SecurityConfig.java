package com.namuk.gadget.security.config;

import com.namuk.gadget.repository.token.RefreshTokenRepository;
import com.namuk.gadget.security.filters.CustomLogoutFilter;
import com.namuk.gadget.security.filters.JsonAuthenticationFilter;
import com.namuk.gadget.security.filters.JwtAuthenticationFilter;
import com.namuk.gadget.security.handler.JsonLoginFailureHandler;
import com.namuk.gadget.security.handler.JsonLoginSuccessHandler;
import com.namuk.gadget.security.jwt.CustomJwtUserDetailService;
import com.namuk.gadget.security.jwt.JwtIssuer;
import com.namuk.gadget.security.jwt.JwtRefreshIssuer;
import com.namuk.gadget.security.jwt.JwtVerificationFilter;
import com.namuk.gadget.security.userDetailService.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Deprecated WebSecurityConfigurerAdapter
 * WebSecurity Configure -> @Bean WebSecurityCustomizer
 *
 * 기존에는 WebSecurityConfigurerAdapter를 상속받아 설정을 오버라이딩 하는 방식
 * 바뀐 방식에서는 상속받아 오버라이딩하지 않고 모두 Bean으로 등록
 */
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
// @EnableWebMvc
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    private final CustomJwtUserDetailService customJwtUserDetailService;

    private final AuthenticationConfiguration configuration;

    private final JwtIssuer jwtIssuer;

    private final JwtRefreshIssuer jwtRefreshIssuer;

    private final RefreshTokenRepository refreshTokenRepository;

    // private final JwtVerificationFilter jwtVerificationFilter;

    // private final ObjectMapper objectMapper;

//    @Autowired
//    public SecurityConfig(CustomUserDetailService customUserDetailService, AuthenticationConfiguration configuration) {
//        this.customUserDetailService = customUserDetailService;
//        this.configuration = configuration;
//    }

    /**
     * Spring Security 적용 무시
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/search/**")
                // .requestMatchers("/login/**")
                .requestMatchers("/user/**")
                .requestMatchers("/users/**")
                .requestMatchers("/error")
                );
                // Boot 3.x after .antMatchers(), .mvcMatchers() 없어지고, requestMatchers()를 사용하도록 바뀌었습니다.
    }

    /**
     *
     * @param http
     * @return 위 설정을 기반으로 한 SecurityFilterChain 반환
     * @throws Exception
     */
    @Bean(name = "SecurityFilterChain")
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/secured/login")
                // csrf -> csrf.disable()
                .csrf(AbstractHttpConfigurer::disable) // CSRF(Cross-Site Request Forgery) 비활성화, post, put 요청 허용
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // RestAPI 서버 방식이기 때문에 Form 기반 로그인 비활성화
                // 커스텀 필터인 jsonAuthenticationFilter를 Spring Security FilterChain의 LogoutFilter 필터 뒤에 등록
                // .addFilterAfter(jsonAuthenticationFilter(), LogoutFilter.class)
                .addFilterAfter(jsonAuthenticationFilter(), LogoutFilter.class)
                // .sessionManagement(sessionManagementConfigurer // 세션 생성 및 정책 설정
                        // -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //  Boot 3.x after .antMatchers(), .mvcMatchers() 없어지고, requestMatchers()를 사용하도록 바뀌었습니다.
                .authorizeHttpRequests(authz -> authz // HTTP 요청에 대한 권한을 설정
                        // 경로 요청에 대한 권한 설정
                        // 해당 경로의 모든 요청은 인증되지 않은 사용자에게 허용
                        .requestMatchers("/secured/login").permitAll()
                        // .requestMatchers에서 지정된 Url 외 요청에 대한 설정
                        // 인증된 사용자에게만 허용, 즉 로그인해야만 접근 가능
                        .anyRequest().authenticated()
                );

        return http.build();

        // .permitAll(): 권한 없이 접속이 가능하다.
        // .authenticated(): 로그인이 필요하다.
        // .hasRole("ROLE"): 특정 권한이 있어야 접속이 가능하다. "ROLE_" 없이 역할 이름만 작성
    }

    @Bean(name = "JwtFilterChain")
    public SecurityFilterChain JwtFilterChain(@Qualifier("JwtAuthenticationManager") AuthenticationManager authenticationManager, HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login", "/join", "/Auth/login", "/admin", "/reissue/refresh", "/logout")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new CustomLogoutFilter(jwtRefreshIssuer, refreshTokenRepository), LogoutFilter.class)
                .addFilterBefore(new JwtVerificationFilter(jwtIssuer), JwtAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter(jwtauthenticationManager(configuration), jwtIssuer, jwtRefreshIssuer), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        return http.build();
    }


    /**
     * AuthenticationManager 인증 매니저 빈 구성
     * 로그인 요청이 있을 때 사용자가 제공한 인증 정보(아이디, 비밀번호)를 기반으로 사용자를 인증하는 역할
     *
     * // @param builder
     * @return 성공하면 Authentication 객체 반환, Authentication 객체는 SecurityContextHolder에 저장되어 현재 사용자의 인증 정보를 유지
     * @throws Exception
     */
    @Bean(name = "securityAuthenticationManager")
    @Primary
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService); // customUserDetailService를 사용하여 사용자 정보를 가져올 수 있도록 설정
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // 패스워드를 인코딩하는데 사용되는 PasswordEncoder를 설정
        return new ProviderManager(daoAuthenticationProvider);
    }

//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        DaoAuthenticationProvider daoAuthenticationProvider = daoAuthenticationProvider();
//        return new ProviderManager(daoAuthenticationProvider);
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
//
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//
//        return daoAuthenticationProvider;
//    }

    /**
     * PasswordEncoder는 사용자의 비밀번호를 해시(hashing) 하거나 인코딩(encoding) 하는 데 사용
     *
     * 암호를 안전하게 저장하기 위해 비밀번호를 해시 값으로 변경하고,
     * 로그인 시 입력된 비밀번호를 같은 방식으로 해시하여 저장된 해시 값과 비교함으로써 인증을 수행
     *
     * @return
     */
    @Bean(name = "SecurityPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 단방향 해시 함수인 BCrypt 사용
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(JsonLoginSuccessHandler());
        jsonAuthenticationFilter.setAuthenticationFailureHandler(JsonLoginFailureHandler());
        return jsonAuthenticationFilter;
    }
    @Bean
    public JsonLoginSuccessHandler JsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler();
    }

    @Bean
    public JsonLoginFailureHandler JsonLoginFailureHandler() {
        return new JsonLoginFailureHandler();
    }

    @Bean(name = "JwtPasswordEncoder")
    public PasswordEncoder JwtpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(@Qualifier("JwtAuthenticationManager") AuthenticationManager authenticationManager, JwtIssuer jwtIssuer,
                                                           JwtRefreshIssuer jwtRefreshIssuer) {
        return new JwtAuthenticationFilter(authenticationManager, jwtIssuer, jwtRefreshIssuer);
    }

    @Bean(name = "JwtAuthenticationManager")
    public AuthenticationManager jwtauthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customJwtUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // return configuration.getAuthenticationManager();
        return new ProviderManager(daoAuthenticationProvider);
    }
}
