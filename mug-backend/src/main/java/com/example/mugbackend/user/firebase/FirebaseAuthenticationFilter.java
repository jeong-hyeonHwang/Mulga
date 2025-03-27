package com.example.mugbackend.user.firebase;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.mugbackend.common.exception.MulgaException;
import com.example.mugbackend.common.exception.TokenInvalidException;
import com.example.mugbackend.common.exception.TokenUnprovidedException;
import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.dto.CustomUserDetails;
import com.example.mugbackend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
	private final UserService userService;
	private final FirebaseService firebaseService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		String method = request.getMethod();
		String authorizationHeader = request.getHeader("Authorization");

		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new TokenUnprovidedException();
		}

		String uid = null;
		try {
			FirebaseService.DecodedToken decodedToken = firebaseService.parseToken(authorizationHeader);
			uid = decodedToken.uid();
		} catch (TokenInvalidException e) {
			throw new TokenInvalidException();
		}

		if ("POST".equalsIgnoreCase(method) && requestUri.equals("/signup")) {
			CustomUserDetails userDetails = CustomUserDetails.builder().id(uid).build();

			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);

			return;
		}

		try {
			User userOpt = userService.findUserById(uid);
			CustomUserDetails userDetails = CustomUserDetails.of(userOpt);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		catch (MulgaException e) {
			throw new MulgaException(e.getErrorCode());
		}
		catch (Exception e) {
			throw new TokenInvalidException();
		}

		filterChain.doFilter(request, response);
	}
}
