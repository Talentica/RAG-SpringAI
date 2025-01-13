package com.openAi.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

@Component
public class CustomHttpServletRequestFactory {

    public SecurityContextHolderAwareRequestWrapper create(HttpServletRequest request, HttpServletResponse response) {
        return new SecurityContextHolderAwareRequestWrapper(request, response.toString());
    }
}