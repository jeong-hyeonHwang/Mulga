package com.example.mugbackend.common.exception;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try{
			filterChain.doFilter(request,response);
		} catch (MulgaException ex){
			setErrorResponse(response, ex.getErrorCode());
		}catch (RuntimeException ex){
			setErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json");

		String jsonBody = "{\"code\":\"" + errorCode.getCode() + "\"}";
		try{
			response.getWriter().write(jsonBody);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
