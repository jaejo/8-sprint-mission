package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private Instant modifiedAt;

  private String username;
  private String password;
  private String email;
  private UUID profileId;

  public User(String username, String password, String email, UUID profileId) {
    id = UUID.randomUUID();
    createdAt = Instant.now();
    modifiedAt = createdAt;

    this.username = username;
    this.password = password;
    this.email = email;
    this.profileId = profileId;
  }

  public void update(String username, String password, String email, UUID profileId) {
    boolean anyValueUpdated = false;

    if (username != null && !username.equals(this.username)) {
      this.username = username;
      anyValueUpdated = true;
    }
    if (password != null && !password.equals(this.password)) {
      this.password = password;
      anyValueUpdated = true;
    }
    if (email != null && !email.equals(this.email)) {
      this.email = email;
      anyValueUpdated = true;
    }
    if (profileId != null && !profileId.equals(this.profileId)) {
      this.profileId = profileId;
    }
    if (anyValueUpdated) {
      this.modifiedAt = Instant.now();
    }
  }
}
