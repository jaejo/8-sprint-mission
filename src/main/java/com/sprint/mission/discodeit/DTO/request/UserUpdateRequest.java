package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 User 정보")
public record UserUpdateRequest(
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    String newUsername,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String newEmail,

    @Size(min = 8, message = "Password must be at least 8 characters long")
    String newPassword
) {

}
