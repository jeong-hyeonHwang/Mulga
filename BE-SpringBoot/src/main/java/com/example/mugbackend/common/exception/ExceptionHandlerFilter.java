package com.example.mugbackend.common.exception;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) {


		try{
			filterChain.doFilter(request,response);
		} catch (MulgaException ex){
			System.out.println(ex.getErrorCode());
			System.out.println(ex.getMessage());
			setErrorResponse(response, ex.getErrorCode());
		} catch (Exception ex){
			System.out.println(ex.getMessage());
			setErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json");

		String jsonBody = "{\"code\":\"" + errorCode.getCode() + "\"}";
		try{
			response.getWriter().write(jsonBody);
			response.flushBuffer();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
