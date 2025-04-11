package com.example.mugbackend.user.dto;

import com.example.mugbackend.user.domain.User;
import lombok.Builder;

@Builder
public record UserDetailDto (
    String id,
    String name,
    Integer budget,
    String email,
    Boolean receivesNotification
){
    public static UserDetailDto of(User user) {
        return UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .budget(user.getBudget())
                .email(user.getEmail())
                .receivesNotification(user.getReceivesNotification())
                .build();
    }
}
