package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.DTO.response.UserStatusResponse;
import com.sprint.mission.discodeit.DTO.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  public UserStatusResponse create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
    if (userStatusRepository.existsByUserId(request.userId())) {
      throw new IllegalStateException("해당 사용자에 대한 UserStatus가 존재합니다.");
    }
    UserStatus userStatus = new UserStatus(request.userId(), request.lastAccessAt());
    UserStatus savedUserstatus = userStatusRepository.save(userStatus);
    return UserStatusResponse.from(savedUserstatus);
  }

  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus입니다."));
  }

  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastAccessAt = request.newLastAccessAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userStatus입니다."));
    userStatus.update(newLastAccessAt);

    return userStatusRepository.save(userStatus);
  }

  public UserStatusResponse updateUserStatusByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastAccessAt = request.newLastAccessAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("해당하는 유저에 대한 UserStatus가 없습니다."));

    userStatus.update(newLastAccessAt);
    return UserStatusResponse.from(userStatusRepository.save(userStatus));
  }

  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userState입니다."));
    userStatusRepository.deleteById(userStatusId);
  }
}
