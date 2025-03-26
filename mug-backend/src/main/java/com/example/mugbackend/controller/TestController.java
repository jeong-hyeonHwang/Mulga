package com.example.mugbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@Value("${spring.data.mongodb.uri}")
	private String uri;

	@GetMapping
	public String test() {
		return uri;
	}
}
