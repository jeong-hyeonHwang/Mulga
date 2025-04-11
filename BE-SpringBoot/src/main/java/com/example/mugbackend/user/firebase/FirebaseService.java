package com.example.mugbackend.user.firebase;

import org.springframework.stereotype.Service;

import com.example.mugbackend.common.exception.TokenInvalidException;
import com.example.mugbackend.common.exception.TokenUnprovidedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseService {
	private final FirebaseAuth firebaseAuth;

	public DecodedToken parseToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new TokenUnprovidedException();
		}

		String token = authorizationHeader.substring(7);

		try {
			FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
			String uid = firebaseToken.getUid();
			String email = (String)firebaseToken.getClaims().get("email");
			return new DecodedToken(uid, email);
		} catch (FirebaseAuthException e) {
			throw new TokenInvalidException();
		}
	}

	public record DecodedToken(String uid, String email) {
	}
}

