package com.openAi.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;

@RestController
@RequestMapping("/security-config")
@EnableCaching
@Slf4j
@RequiredArgsConstructor
public class ConfigController {

    private final com.openAi.security.WebSecurityConfig webSecurityConfig;

    @PostMapping("/refresh-cors-config")
    public ResponseEntity<String> refreshCorsConfig() {
        CorsConfiguration configuration = new CorsConfiguration();
        webSecurityConfig.updateCorsConfiguration(configuration);
        return ResponseEntity.ok("CORS configuration updated successfully");
    }
}
