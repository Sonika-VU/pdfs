package com.example.demo.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        // Get current request attributes
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        if (request == null) {
            return;
        }

        // Extract Authorization header from incoming request
        String authorizationHeader = request.getHeader("Authorization");

        // If header exists, forward it to Loan service
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            template.header("Authorization", authorizationHeader);
        }
    }
}