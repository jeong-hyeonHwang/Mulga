package com.example.mugbackend.user.dto;

import com.example.mugbackend.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserUpdateDto (
    @Nullable @NotBlank String name,
    Integer budget,
    Boolean receivesNotification
) {
    public void applyChangesToUser(User user) {
        if(receivesNotification != null)
            user.setReceivesNotification(receivesNotification);
        if(name != null)
            user.setName(name);
        if(budget != null)
            user.setBudget(budget);
    }
}
