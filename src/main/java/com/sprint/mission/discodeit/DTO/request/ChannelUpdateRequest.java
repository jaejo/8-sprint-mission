package com.sprint.mission.discodeit.DTO.request;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id,
        String channelName,
        String description,
        int participant,
        List<String> participants
) {
}
