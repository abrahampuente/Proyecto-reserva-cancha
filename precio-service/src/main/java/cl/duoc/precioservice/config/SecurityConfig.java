package cl.duoc.precioservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/precios/**").hasAnyRole("ADMIN", "DUENIO")
                .requestMatchers(HttpMethod.PUT, "/api/precios/**").hasAnyRole("ADMIN", "DUENIO")
                .requestMatchers(HttpMethod.DELETE, "/api/precios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/precios/**").hasAnyRole("ADMIN", "DUENIO", "CLIENTE")
                .anyRequest().authenticated())
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint))
            .httpBasic(basic -> {});
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails duenio = User.builder()
                .username("duenio")
                .password(encoder.encode("duenio123"))
                .roles("DUENIO")
                .build();

        UserDetails cliente = User.builder()
                .username("cliente")
                .password(encoder.encode("cliente123"))
                .roles("CLIENTE")
                .build();

        return new InMemoryUserDetailsManager(admin, duenio, cliente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
