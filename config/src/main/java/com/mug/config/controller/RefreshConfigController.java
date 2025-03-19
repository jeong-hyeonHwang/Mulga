package com.mug.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.server.environment.JGitEnvironmentRepository;
import org.springframework.cloud.config.server.environment.MultipleJGitEnvironmentRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class RefreshConfigController {

	@Autowired
	private ApplicationContext context;

	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refreshGitRepository() {
		Map<String, String> response = new HashMap<>();

		try {
			Map<String, MultipleJGitEnvironmentRepository> multipleGitRepos =
				context.getBeansOfType(MultipleJGitEnvironmentRepository.class);

			Map<String, JGitEnvironmentRepository> singleGitRepos =
				context.getBeansOfType(JGitEnvironmentRepository.class);

			boolean gitRepoFound = false;

			for (MultipleJGitEnvironmentRepository repo : multipleGitRepos.values()) {
				repo.afterPropertiesSet();
				gitRepoFound = true;
			}

			for (JGitEnvironmentRepository repo : singleGitRepos.values()) {
				if (!(repo instanceof MultipleJGitEnvironmentRepository)) { // 중복 방지
					repo.afterPropertiesSet();
					gitRepoFound = true;
				}
			}

			if (gitRepoFound) {
				response.put("status", "success");
				response.put("message", "Git 저장소가 성공적으로 재클론되었습니다");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", "error");
				response.put("message", "Git 저장소 구현을 찾을 수 없습니다");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Git 저장소 재클론 실패: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
