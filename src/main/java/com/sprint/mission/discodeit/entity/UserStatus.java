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
  private final Instant createdAt;
  private Instant updatedAt;

  private final UUID userId;
  private Instant lastAccessAt;

  public UserStatus(UUID userId, Instant lastAccessAt) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

    this.userId = userId;
    this.lastAccessAt = lastAccessAt;
  }

  public void update(Instant lastAccessAt) {
    boolean anyValueUpdated = false;
    if (lastAccessAt != null && !lastAccessAt.equals(this.lastAccessAt)) {
      this.lastAccessAt = lastAccessAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public boolean isOnline() {
    return Duration.between(lastAccessAt, Instant.now()).toMinutes() <= SESSION_TIMEOUT_MINUTES;
  }
}
