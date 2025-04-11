package com.example.mugbackend.common.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

@Configuration
public class FirebaseConfig {
	@Value("${firebase.credentials.type}")
	private String type;

	@Value("${firebase.credentials.projectId}")
	private String projectId;

	@Value("${firebase.credentials.privateKeyId}")
	private String privateKeyId;

	@Value("${firebase.credentials.privateKey}")
	private String privateKey;

	@Value("${firebase.credentials.clientEmail}")
	private String clientEmail;

	@Value("${firebase.credentials.clientId}")
	private String clientId;

	@Value("${firebase.credentials.authUri}")
	private String authUri;

	@Value("${firebase.credentials.tokenUri}")
	private String tokenUri;

	@Value("${firebase.credentials.authProviderX509CertUrl}")
	private String authProviderX509CertUrl;

	@Value("${firebase.credentials.clientX509CertUrl}")
	private String clientX509CertUrl;

	@Value("${firebase.credentials.universeDomain}")
	private String universeDomain;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		List<FirebaseApp> apps = FirebaseApp.getApps();
		if (apps.isEmpty()) {
			try {
				String credentialsJson = createCredentialsJson();
				ByteArrayInputStream serviceAccount =
					new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));

				FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

				return FirebaseApp.initializeApp(options);

			} catch (Exception e) {
				throw new IOException("Failed to initialize Firebase: " + e.getMessage(), e);
			}
		}

		return FirebaseApp.getInstance();
	}

	private String createCredentialsJson() {
		Map<String, String> credentials = new HashMap<>();

		credentials.put("type", type);
		credentials.put("project_id", projectId);
		credentials.put("private_key_id", privateKeyId);
		credentials.put("private_key", privateKey);
		credentials.put("client_email", clientEmail);
		credentials.put("client_id", clientId);
		credentials.put("auth_uri", authUri);
		credentials.put("token_uri", tokenUri);
		credentials.put("auth_provider_x509_cert_url", authProviderX509CertUrl);
		credentials.put("client_x509_cert_url", clientX509CertUrl);
		credentials.put("universe_domain", universeDomain);

		return new Gson().toJson(credentials);
	}

	@Bean
	public FirebaseAuth getFirebaseAuth() {
		try {
			return FirebaseAuth.getInstance(firebaseApp());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
