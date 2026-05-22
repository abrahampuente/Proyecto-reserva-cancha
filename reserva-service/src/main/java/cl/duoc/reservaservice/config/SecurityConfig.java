package cl.duoc.reservaservice.config;
// Configuración de seguridad con Basic Auth para reserva-service

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()

                        // Interno para pago-service
                        .requestMatchers("/api/reservas/*/exists").permitAll()

                        // Crear reserva
                        .requestMatchers(HttpMethod.POST, "/api/reservas/**")
                        .hasAnyRole("CLIENTE", "ADMIN")

                        // Consultar reservas
                        .requestMatchers(HttpMethod.GET, "/api/reservas/**")
                        .hasAnyRole("CLIENTE", "DUENIO", "ADMIN")

                        // Modificar reserva
                        .requestMatchers(HttpMethod.PUT, "/api/reservas/**")
                        .hasAnyRole("CLIENTE", "ADMIN")

                        // Cancelar reserva
                        .requestMatchers(HttpMethod.DELETE, "/api/reservas/**")
                        .hasAnyRole("CLIENTE", "ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails duenio = User.withUsername("duenio")
                .password(encoder.encode("duenio123"))
                .roles("DUENIO")
                .build();

        UserDetails cliente = User.withUsername("cliente")
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
