package com.sprint.mission.discodeit.DTO.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    UUID profileId,
    Instant createdAt,
    Instant modifiedAt,
    Boolean online
) {

}
