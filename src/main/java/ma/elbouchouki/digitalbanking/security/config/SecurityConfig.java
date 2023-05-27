package ma.elbouchouki.digitalbanking.security.config;

import lombok.AllArgsConstructor;
import ma.elbouchouki.digitalbanking.security.filters.JwtAuthenticationFilter;
import ma.elbouchouki.digitalbanking.security.filters.JwtAuthorizationFilter;
import ma.elbouchouki.digitalbanking.security.service.SecurityService;
import ma.elbouchouki.digitalbanking.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private UserDetailsServiceImpl userDetailsService;
    private SecurityService securityService;

    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(
                        httpSecurityCorsConfigurer -> {
                            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                            CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
                            corsConfiguration.addAllowedMethod("DELETE");
                            corsConfiguration.addAllowedMethod("PUT");
                            corsConfiguration.addAllowedMethod("POST");
                            corsConfiguration.addAllowedMethod("GET");
                            source.registerCorsConfiguration("/**", corsConfiguration);
                            httpSecurityCorsConfigurer.configurationSource(source);
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions().disable())
                .authorizeHttpRequests(ar -> ar.requestMatchers(
                        "/swagger-ui**",
                        "/swagger-ui/**",
                        "/v3/**",
                        "/api/profile",
                        SecurityConstant.REFRESH_PATH,
                        "/api/v1/auth/sign-in",
                        "/api/v1/auth/sign-up"
                ).permitAll())
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityService))
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(daoAuthenticationProvider);
    }

}
