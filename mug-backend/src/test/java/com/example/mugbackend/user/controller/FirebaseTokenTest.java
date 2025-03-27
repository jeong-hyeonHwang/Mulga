package com.example.mugbackend.user.controller;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

@SpringBootTest
@AutoConfigureMockMvc
public class FirebaseTokenTest {
	@Autowired
	private MockMvc mockMvc;

	@Value("${firebase.test.uid}")
	private String uid;

	@Value("${firebase.test.webApiKey}")
	private String firebaseApiKey;

	@Test
	public void createCustomToken() throws FirebaseAuthException {
		String customToken = FirebaseAuth.getInstance().createCustomToken(uid);

		HttpHeaders headers = new HttpHeaders();
		String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + firebaseApiKey;
		String body = String.format("{\"token\":\"%s\",\"returnSecureToken\":true}", customToken);
		headers.set("Content-Type", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
			url, body, Map.class);

		Map<String, Object> responseBody = responseEntity.getBody();
		if (responseBody != null && responseBody.containsKey("idToken")) {
			System.out.println(responseBody.get("idToken"));
		}
	}
}
