package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  /*
    NonNull 추가 이유:
    - 부모 인터페이스인 JpaRepositoryu에 정의된 findAll() 메서드가
    - @NonNull로 선언되어 있기 때문에 오버라이딩한 메서드에도 추가해야함
  */
  @Override
  @EntityGraph(attributePaths = {"profile"})
  @NonNull
  List<User> findAll();

  boolean existsByUsername(String name);

  boolean existsByEmail(String email);
}
