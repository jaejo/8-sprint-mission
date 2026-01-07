package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.DTO.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final PasswordEncoder passwordEncoder;
  private final BinaryContentService binaryContentService;

  @Override
  public UserResponse create(UserCreateRequest request, MultipartFile file) {
    if (userRepository.existsByName(request.username())) {
      throw new IllegalArgumentException(
          "User with name " + request.username() + " already exists");
    }
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("User with email " + request.email() + " already exists.");
    }

    String encodedPassword = passwordEncoder.encode(request.password());

    UUID profileIdNullable = binaryContentService.save(file, "images");

    User user = new User(
        request.username(),
        encodedPassword,
        request.email(),
        profileIdNullable
    );

    User savedUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus status = new UserStatus(savedUser.getId(), now);
    userStatusRepository.save(status);

    return UserResponse.from(savedUser, status);
  }


  @Override
  public UserResponse findById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당하는 유저를 찾을 수 없습니다."));

    UserStatus status = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("해당하는 userStatus를 찾을 수 없습니다."));

    return UserResponse.from(user, status);
  }

  @Override
  public List<UserResponse> findAll() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(user -> {
          UserStatus status = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(() -> new NoSuchElementException("해당하는 userStatus를 찾을 수 없습니다."));
          return UserResponse.from(user, status);
        })
        .toList();
  }

  @Override
  public UserResponse update(UUID userId, UserUpdateRequest request, MultipartFile file) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("수정하려는 유저가 없습니다."));

    // 기존 유저의 이름과 요청한 이름이 다를 경우, 같으면 넘어감
    if (!user.getUsername().equals(request.newUsername())) {
      if (userRepository.existsByName(request.newUsername())) {
        throw new IllegalArgumentException("이미 " + request.newUsername() + "을 가진 유저가 존재합니다.");
      }
    }

    // 기존 유저의 이메일과 요청한 이메일 다를 경우, 같으면 넘어감
    if (!user.getEmail().equals(request.newEmail())) {
      if (userRepository.existsByEmail(request.newEmail())) {
        throw new IllegalArgumentException("이미 " + request.newEmail() + "을 가진 유저가 존재합니다.");
      }
    }

    UUID newProfileId = binaryContentService.save(file, "images");

    String encodedPassword = user.getPassword();
    if (request.newPassword() != null) {
      encodedPassword = passwordEncoder.encode(request.newPassword());
    }

    user.update(request.newUsername(), encodedPassword, request.newEmail(), newProfileId);

    User savedUser = userRepository.save(user);
    UserStatus status = userStatusRepository.findByUserId(savedUser.getId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus입니다."));
    return UserResponse.from(savedUser, status);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("삭제하려는 유저가 없습니다."));

    userStatusRepository.deleteByUserId(user.getId());

    if (user.getProfileId() != null) {
      binaryContentRepository.delete(user.getProfileId());
    }

    userRepository.delete(userId);
  }
}
