package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Channel 정보")
public record PublicChannelUpdateRequest(
    @NotBlank(message = "Channel name is required")
    @Size(min = 2, max = 100, message = "Channel name must be between 2 and 100 characters")
    String newName,

    @Size(max = 500, message = "Description must be less than 500 characters")
    String newDescription
) {

}
