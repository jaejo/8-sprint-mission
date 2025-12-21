package com.sprint.mission.discodeit.DTO.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID uId,
        UUID cId

) {
    public static ReadStatusResponse from(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUId(),
                readStatus.getCId()
        );
    }
}
