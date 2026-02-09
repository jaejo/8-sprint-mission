package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    @NotNull(message = "Author ID is required")
    UUID authorId,

    @NotNull(message = "Channel ID is required")
    UUID channelId,

    @Size(max = 2000, message = "Content must be less than 2000 characters")
    String content
) {

}
