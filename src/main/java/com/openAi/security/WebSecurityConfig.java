//package com.openAi.security;
//
//import com.openAi.security.entity.AllowedDomain;
//import com.openAi.security.repository.AllowedDomainRepository;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@RequiredArgsConstructor
//public class WebSecurityConfig  {
//
//  private final AuthenticationFilter filter;
//
//  private final AllowedDomainRepository allowedDomainRepository;
//
//  private CorsConfiguration configuration = new CorsConfiguration();
//
//
////  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers(
//                            "/getToken",
//                            "/cicero/cicero-compliance-data",
//                            "/cicero/importCiceroData",
//                            "/technology/getTechDigest/label",
//                            "/valueAdd/valueAddsByTag",
//                            "/tech/categories",
//                            "/tech/categories/*",
//                            "/tech/category/*/value-adds",
//                            "/tech/category/*/blogs",
//                            "/tech/category/*/teams",
//                            "/tech/category/**",
//                            "/tech/**",
//                            "/getTokenUserAuth",
//                            "/user/generatePassword",
//                            "/resetPassword",
//                            "/getTokenGoogleAuth"
//                    ).permitAll()
//                    .anyRequest().authenticated()
//            )
//            .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//            .addFilterBefore(filter, AuthenticationFilter.class);
//  }
//  @Bean
//  @Profile({"dev","staging","prod"})
//  public CorsConfigurationSource corsConfigurationSource() {
//    updateCorsConfiguration();
//    configuration.addExposedHeader("Set-Cookie");
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
//    // setAllowCredentials(true) is important, otherwise:
//    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
//    // wildcard '*' when the request's credentials mode is 'include'.
//    // setAllowedHeaders is important! Without it, OPTIONS preflight request
//    // will fail with 403 Invalid CORS request
//    configuration.setAllowedHeaders(
//        List.of(
//            "Api-Key",
//            "Authorization",
//            "Cache-Control",
//            "Content-Type",
//            "Access-Control-Allow-Origin",
//            "Access-Control-Allow-Credentials"));
//    configuration.setAllowCredentials(true);
//
//    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }
//
//  @Bean
//  @Profile("test")
//  public CorsConfigurationSource corsConfigurationSourceTest() {
//    final CorsConfiguration corsConfiguration = new CorsConfiguration();
//    corsConfiguration.setAllowedOrigins(
//            List.of(
//                    "https://playbook.talentica.com/",
//                    "https://orion.talentica.com/",
//                    "https://prod-orion.talentica.com/",
//                    "http://localhost:3000/",
//                    "http://localhost/",
//                    "https://stagingplaybook.talentica.com",
//                    "https://dev-orion.talentica.com/",
//                    "https://stagingorion.talentica.com",
//                    "https://staging-orion.talentica.com/",
//                    "http://localhost:3001/",
//                    "https://stagingknowledgebase.talentica.com/",
//                    "https://stagingknowledgebase-sales.talentica.com/",
//                    "https://staging.kb.talentica.com/",
//                    "https://stagingknowledgebase.talentica.com/",
//                    "https://nexus.talentica.com/",
//                    "https://kb.talentica.com/"));
//    corsConfiguration.addExposedHeader("Set-Cookie");
//    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
//    // setAllowCredentials(true) is important, otherwise:
//    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
//    // wildcard '*' when the request's credentials mode is 'include'.
//    // setAllowedHeaders is important! Without it, OPTIONS preflight request
//    // will fail with 403 Invalid CORS request
//    corsConfiguration.setAllowedHeaders(
//            List.of(
//                    "Api-Key",
//                    "Authorization",
//                    "Cache-Control",
//                    "Content-Type",
//                    "Access-Control-Allow-Origin",
//                    "Access-Control-Allow-Credentials"));
//    corsConfiguration.setAllowCredentials(true);
//
//    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", corsConfiguration);
//    return source;
//  }
//
//  public void updateCorsConfiguration() {
//    List<String> origins = allowedDomainRepository.findAll()
//            .stream().map(AllowedDomain::getDomainName).toList();
//    configuration.setAllowedOrigins(origins);
//  }
//}
