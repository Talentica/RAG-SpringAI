package com.openAi.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityContextHolderAwareRequestFilter extends SecurityContextHolderAwareRequestFilter {

    private final CustomHttpServletRequestFactory customHttpServletRequestFactory;

    public CustomSecurityContextHolderAwareRequestFilter(CustomHttpServletRequestFactory customHttpServletRequestFactory) {
        this.customHttpServletRequestFactory = customHttpServletRequestFactory;
    }
//    @Override
    protected SecurityContextHolderAwareRequestWrapper wrapRequest(HttpServletRequest request) {
        return customHttpServletRequestFactory.create(request, null);
    }

//    @Override
    protected SecurityContextHolderAwareRequestWrapper wrapRequest(HttpServletRequest request, HttpServletResponse response) {
        return customHttpServletRequestFactory.create(request, response);
    }

//    @Override
    protected SecurityContextHolderAwareRequestWrapper create(HttpServletRequest request, HttpServletResponse response) {
        return customHttpServletRequestFactory.create(request, response);
    }
}