package com.openAi.security;

import com.openAi.security.entity.AllowedDomain;
import com.openAi.security.repository.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
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
@EnableWebSecurity
public class WebSecurityConfig {

  private final AllowedDomainRepository allowedDomainRepository;

  private final CorsConfiguration configuration = new CorsConfiguration();

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
  public AuthenticationConfiguration authenticationConfiguration(ObjectPostProcessor<Object> objectPostProcessor) {
    AuthenticationConfiguration authenticationConfiguration = new AuthenticationConfiguration();
    authenticationConfiguration.setObjectPostProcessor(objectPostProcessor);
    return authenticationConfiguration;
  }

  @Bean
  public ObjectPostProcessor<Object> objectPostProcessor() {
    return new ObjectPostProcessor<Object>() {
      @Override
      public <O extends Object> O postProcess(O object) {
        return object;
      }
    };
  }

  @Bean
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
  @Bean
  public AuthenticationManagerBuilder authenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
    return new AuthenticationManagerBuilder(objectPostProcessor);
  }
  @Bean
  public UsernamePasswordAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
    UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }
}