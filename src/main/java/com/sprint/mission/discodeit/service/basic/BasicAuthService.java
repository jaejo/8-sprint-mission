package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.request.LoginRequest;
import com.sprint.mission.discodeit.Exception.InvalidPasswordException;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new InvalidPasswordException();
    }

    return user;
  }
}
