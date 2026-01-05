package com.sprint.mission.discodeit.DTO.request;

import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId
) {

}
