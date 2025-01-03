package com.openAi.security;

import com.openAi.security.entity.AllowedDomain;
import com.openAi.security.repository.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationFilter filter;

  private final AllowedDomainRepository allowedDomainRepository;

  private CorsConfiguration configuration = new CorsConfiguration();

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/getToken",
                "/getTokenUserAuth",
                "/user/generatePassword",
                "/resetPassword",
                "/getTokenGoogleAuth"
                )
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Profile({"dev","staging","prod"})
  public CorsConfigurationSource corsConfigurationSource() {
    updateCorsConfiguration();
    configuration.addExposedHeader("Set-Cookie");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    // setAllowCredentials(true) is important, otherwise:
    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
    // wildcard '*' when the request's credentials mode is 'include'.
    // setAllowedHeaders is important! Without it, OPTIONS preflight request
    // will fail with 403 Invalid CORS request
    configuration.setAllowedHeaders(
        List.of(
            "Api-Key",
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"));
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
                    "https://kb.talentica.com/"));
    configuration.addExposedHeader("Set-Cookie");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    // setAllowCredentials(true) is important, otherwise:
    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
    // wildcard '*' when the request's credentials mode is 'include'.
    // setAllowedHeaders is important! Without it, OPTIONS preflight request
    // will fail with 403 Invalid CORS request
    configuration.setAllowedHeaders(
            List.of(
                    "Api-Key",
                    "Authorization",
                    "Cache-Control",
                    "Content-Type",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"));
    configuration.setAllowCredentials(true);

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  public void updateCorsConfiguration() {
    List<String> origins = allowedDomainRepository.findAll()
            .stream().map(AllowedDomain::getDomainName).toList();
    configuration.setAllowedOrigins(origins);
  }
}
