package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  // @DataJpaTest는 기본적으로 JPA Auditing을 활성화하지 않으므로 별도 설정 필요
  @Configuration
  @EnableJpaAuditing
  static class TestConfig {

  }

  @Test
  @DisplayName("유저 저장 및 조회 테스트")
  void save_and_find_user() {
    // given
    User user = new User("testUser", "test@example.com", "password123", null);

    // when
    User savedUser = userRepository.save(user);

    // then
    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getUsername()).isEqualTo("testUser");
    assertThat(savedUser.getEmail()).isEqualTo("test@example.com");

    Optional<User> foundUser = userRepository.findById(savedUser.getId());
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
  }

  @Test
  @DisplayName("유저명 중복 확인 테스트")
  void exists_by_username() {
    // given
    User user = new User("duplicateUser", "dup@example.com", "password123", null);
    userRepository.save(user);

    // when
    boolean exists = userRepository.existsByUsername("duplicateUser");
    boolean notExists = userRepository.existsByUsername("newUser");

    // then
    assertThat(exists).isTrue();
    assertThat(notExists).isFalse();
  }
}
