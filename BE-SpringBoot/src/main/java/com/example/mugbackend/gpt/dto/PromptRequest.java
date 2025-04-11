package com.example.mugbackend.gpt.dto;

import lombok.Builder;

@Builder
public record PromptRequest(String prompt) {

}
