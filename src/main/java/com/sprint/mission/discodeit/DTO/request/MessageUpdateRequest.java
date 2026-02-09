package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Message 내용")
public record MessageUpdateRequest(
    @Size(max = 2000, message = "Content must be less than 2000 characters")
    String newContent
) {

}
