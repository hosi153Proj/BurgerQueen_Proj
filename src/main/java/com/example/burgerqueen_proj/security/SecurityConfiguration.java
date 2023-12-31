package com.example.burgerqueen_proj.security;


import com.example.burgerqueen_proj.member.service.MemberService;
import com.example.burgerqueen_proj.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity

public class SecurityConfiguration {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final JwtTokenProvider jwtTokenProvider;


//    public SecurityConfiguration(@Lazy PrincipalOauth2UserService principalOauth2UserService) {

    public SecurityConfiguration(@Lazy PrincipalOauth2UserService principalOauth2UserService, JwtTokenProvider jwtTokenProvider) {

        this.principalOauth2UserService = principalOauth2UserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .antMatchers("/static/**","/static/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .httpBasic()

                .and()
                .authorizeRequests() //인증과 인가를 설정하겠다는 선언(메서드)
                .antMatchers("/api/admin/authenticated","/api/admin/tokenTest" ).hasRole("ADMIN")
                .antMatchers("/login", "/signup", "/join", "/api/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/home")
                .and()
                .logout()

                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)

                // jwt 필터 추가
                .and()
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )


                .oauth2Login()
                .loginPage("/login")//.defaultSuccessUrl("/home")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)

        ;




        return security.build();

    }



}
