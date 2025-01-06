package com.openAi.security;

import com.openAi.security.entity.AllowedDomain;
import com.openAi.security.repository.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final AuthenticationFilter filter;
  private final AllowedDomainRepository allowedDomainRepository;

  private final CorsConfiguration configuration = new CorsConfiguration();

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors()
            .and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
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
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  @Profile({"dev", "staging", "prod"})
  public CorsConfigurationSource corsConfigurationSource() {
    updateCorsConfiguration();
    configuration.addExposedHeader("Set-Cookie");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    configuration.setAllowedHeaders(
            List.of(
                    "Api-Key",
                    "Authorization",
                    "Cache-Control",
                    "Content-Type",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"
            )
    );
    configuration.setAllowCredentials(true);

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  @Profile("test")
  public CorsConfigurationSource corsConfigurationSourceTest() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
            List.of(
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
                    "https://stagingknowledgebase.talentica.com/",
                    "https://nexus.talentica.com/",
                    "https://kb.talentica.com/"
            )
    );
    configuration.addExposedHeader("Set-Cookie");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    configuration.setAllowedHeaders(
            List.of(
                    "Api-Key",
                    "Authorization",
                    "Cache-Control",
                    "Content-Type",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"
            )
    );
    configuration.setAllowCredentials(true);

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  void updateCorsConfiguration() {
    List<String> origins = allowedDomainRepository.findAll()
            .stream().map(AllowedDomain::getDomainName).toList();
    configuration.setAllowedOrigins(origins);
  }
}
