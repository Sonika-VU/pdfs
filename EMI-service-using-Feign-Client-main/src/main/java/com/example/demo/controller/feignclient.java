package com.example.demo.controller;

import com.example.demo.Dto.LoanResponse;
import com.example.demo.config.FeignConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "loan-service",
        url = "http://localhost:8898",
        configuration = FeignConfiguration.class
        /*fallback = LoanClientFallback.class*/
)
public interface feignclient {
	@GetMapping("/loans/id/{loanId}")
	LoanResponse getLoanById(
	        @RequestHeader("Authorization") String token,
	        @PathVariable("loanId") Long loanId
	);
}