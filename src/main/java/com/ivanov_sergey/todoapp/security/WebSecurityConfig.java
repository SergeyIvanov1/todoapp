package com.ivanov_sergey.todoapp.security;

import com.ivanov_sergey.todoapp.security.jwt.AuthEntryPointJwt;
import com.ivanov_sergey.todoapp.security.jwt.AuthTokenFilter;
import com.ivanov_sergey.todoapp.security.secure_services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true, // например разрешить доступ к нашей конечной точке /admin только пользователям ADMIN - @RolesAllowed("ADMIN") над методом в контроллере
        prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests().antMatchers("/**").permitAll()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/**").permitAll()
//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //    только сайт http://localhost:8081 может делать запросы
//    запрос можно делать абсолютно всеми методами (GET, POST, PUT и т.д.)
//    обращаться к нашему приложению можно по любому внутреннему url — addMapping(«/**»)
//Spring Boot приложение понимает, сайту http://localhost:4200 доступ разрешен
// (это мы настроили в методе addCorsMappings), так что в http-ответ оно включает такой заголовок:
//    Access-Control-Allow-Origin: http://localhost:8081
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("*");
            }
        };
    }

// метод для открытия ресурса/home
//    @Bean
//    public WebSecurityCustomizer ignoreResources() {
//        return (webSecurity) -> webSecurity
//                .ignoring()
//                .antMatchers("/home/*");
//    }
}
