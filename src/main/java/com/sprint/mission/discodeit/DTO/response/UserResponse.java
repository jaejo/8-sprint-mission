package com.sprint.mission.discodeit.DTO.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    Optional<UUID> profileId,
    Instant createdAt,
    Instant modifiedAt,
    Boolean online

) {

  public static UserResponse from(User user, UserStatus userStatus) {
    boolean isOnline = (userStatus != null) && userStatus.isOnline();
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        Optional.ofNullable(user.getProfileId()),
        user.getCreatedAt(),
        user.getModifiedAt(),
        isOnline
    );
  }
}
