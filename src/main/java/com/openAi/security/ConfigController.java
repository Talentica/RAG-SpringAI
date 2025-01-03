package com.openAi.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security-config")
@EnableCaching
@Slf4j
@RequiredArgsConstructor
public class ConfigController {

    private final WebSecurityConfig webSecurityConfig;

    @PostMapping("/refresh-cors-config")
    public ResponseEntity<String> refreshCorsConfig() {
        webSecurityConfig.updateCorsConfiguration();
        return ResponseEntity.ok("CORS configuration updated successfully");
    }
}
