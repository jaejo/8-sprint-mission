package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final int SESSION_TIMEOUT_MINUTES = 5;

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final UUID userId;
    private UserStatusType userStatusType;
    private final Instant createdAt;
    private Instant lastAccessAt;

    public UserStatus(UUID userId) {
        id = UUID.randomUUID();
        userStatusType = null;
        createdAt = Instant.now();
        lastAccessAt = createdAt;
        this.userId = userId;
    }

    public void update(UserStatusType userStatusType) {
        this.userStatusType = userStatusType;
    }

    public boolean isCurrentOnline() {
        return Duration.between(lastAccessAt, Instant.now()).toMinutes() <= SESSION_TIMEOUT_MINUTES;
    }

    public void updateLastAccess() {
        lastAccessAt = Instant.now();
        this.userStatusType = UserStatusType.ONLINE;
    }

    public UserStatusType getCurrentStatus() {
        return isCurrentOnline() ? UserStatusType.ONLINE : UserStatusType.OFFLINE;
    }
}
