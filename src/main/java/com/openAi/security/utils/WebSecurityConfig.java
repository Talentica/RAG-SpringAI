package com.openAi.security;

import com.openAi.security.entity.AllowedDomain;
import com.openAi.security.repository.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

  private final AllowedDomainRepository allowedDomainRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(
                            "/getToken",
                            "/getTokenUserAuth",
                            "/user/generatePassword",
                            "/resetPassword",
                            "/getTokenGoogleAuth"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .addFilterBefore(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    updateCorsConfiguration(configuration);
    applyCommonCorsSettings(configuration);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  @Profile("test")
  public CorsConfigurationSource corsConfigurationSourceTest() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(
            "https://playbook.talentica.com/",
            "https://orion.talentica.com/",
            "https://prod-orion.talentica.com/",
            "http://localhost:3000/",
            "http://localhost/",
            "https://stagingplaybook.talentica.com",
            "https://dev-orion.talentica.com/",
            "https://stagingorion.talentica.com",
            "https://staging-orion.talentica.com/",
            "http://localhost:3001/",
            "https://stagingknowledgebase.talentica.com/",
            "https://stagingknowledgebase-sales.talentica.com/",
            "https://staging.kb.talentica.com/",
            "https://nexus.talentica.com/",
            "https://kb.talentica.com/"
    ));
    applyCommonCorsSettings(configuration);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  public void updateCorsConfiguration(CorsConfiguration configuration) {
    List<String> allowedOrigins = allowedDomainRepository.findAll()
            .stream()
            .map(AllowedDomain::getDomainName)
            .collect(Collectors.toList());
    configuration.setAllowedOrigins(allowedOrigins);
  }

  private void applyCommonCorsSettings(CorsConfiguration configuration) {
    configuration.addExposedHeader("Set-Cookie");
    configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of(
            "Api-Key",
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
    ));
    configuration.setAllowCredentials(true);
  }

  @Bean
  public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
    AuthenticationFilter filter = new AuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }
}
