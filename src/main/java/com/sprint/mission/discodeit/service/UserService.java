package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserResponse create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  // 단건 조회
  UserResponse findById(UUID id);

  //다건 조회
  //- 전체 조회
  List<UserResponse> findAll();

  UserResponse update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  //유저 삭제
  void delete(UUID id);
}
