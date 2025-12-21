package com.sprint.mission.discodeit.DTO.response;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        UUID uId
) {
    public static UserStatusResponse from(UserStatus status) {
        return new UserStatusResponse(
                status.getId(),
                status.getUId()
        );
    }
}
