package com.example.dinewise.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.config.Customizer;

import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import com.example.dinewise.service.MessManagerService;
import com.example.dinewise.service.StudentService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.List;


@Configuration
public class SecurityConfig {

    private final StudentService studentService;
    private final JwtGeneratorImpl jwtGenerator;
    private final MessManagerService messManagerService;

    public SecurityConfig(StudentService studentService, MessManagerService messManagerService, JwtGeneratorImpl jwtGenerator) {
        this.studentService = studentService;
        this.jwtGenerator = jwtGenerator;
        this.messManagerService = messManagerService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable()) // Disable CSRF for API usage like Postman
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/signup","/login").permitAll() // ✅ allow signup
    //             .anyRequest().authenticated() // other endpoints need auth
    //         );

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // ✅ enable CORS
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup/request","/signup/verify", "/login","/manager/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class); // 🔑 custom filter

        return http.build();
    }

    @Bean
    public OncePerRequestFilter jwtAuthFilter() {
        return new OncePerRequestFilter() {



            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {

                String token = null;

                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("authToken".equals(cookie.getName())) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }

                if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        Claims claims = Jwts.parser()
                                .setSigningKey(jwtGenerator.key())
                                .parseClaimsJws(token)
                                .getBody();

                        String subject = claims.getSubject();
                        String role = claims.get("role", String.class);

                        if(role!=null){
                            System.out.println("Role is not  null in JWT claims");
                        }

                        if(role==null){
                            System.out.println("Roll is null");
                        }

                        if (role.equals("student")) {
                            Student student = studentService.getStudentByUsername(subject);
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(student, null, List.of());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } else if (role.equals("manager")) {
                            MessManager manager = messManagerService.getByStdId(subject);
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(manager, null, List.of());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }

                    } catch (Exception ex) {
                        System.out.println("Invalid JWT Token: " + ex.getMessage());
                    }
                }

                filterChain.doFilter(request, response);
            }
        };




        //     @Override
        //     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        //             throws ServletException, IOException {

        //         String token = null;

        //         // ✅ Extract token from 'authToken' cookie
        //         if (request.getCookies() != null) {
        //             for (Cookie cookie : request.getCookies()) {
        //                 if ("authToken".equals(cookie.getName())) {
        //                     token = cookie.getValue();
        //                     break;
        //                 }
        //             }
        //         }

        //         if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        //             try {
        //                 String username = jwtGenerator.getUsernameFromToken(token); // implement this
        //                 if (username != null) {
        //                     Student student = studentService.getStudentByUsername(username); // fetch from DB

        //                     UsernamePasswordAuthenticationToken authToken =
        //                             new UsernamePasswordAuthenticationToken(student, null, List.of()); // no roles here
        //                     SecurityContextHolder.getContext().setAuthentication(authToken);
        //                 }
        //             } catch (Exception ex) {
        //                 System.out.println("Invalid JWT Token: " + ex.getMessage());
        //             }
        //         }

        //         filterChain.doFilter(request, response);
        //     }
        // };
    }



    
}


