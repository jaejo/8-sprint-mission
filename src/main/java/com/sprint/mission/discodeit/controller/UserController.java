package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.DTO.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "User 등록", operationId = "create")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "User가 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = UserResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(
              examples = @ExampleObject(value = "User with email {email} already exists")
          )
      )
  })
  @RequestMapping(method = RequestMethod.POST, consumes = {
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponse> create(
      @Parameter(description = "User 생성 요청 정보", required = true)
      @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      @Parameter(description = "User 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(userCreateRequest, profile));
  }

  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResponseEntity<UserResponse> findById(@RequestParam(value = "id") UUID id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.findById(id));
  }

  @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
  @ApiResponse(
      responseCode = "200",
      description = "User 목록 조회 성공",
      content = @Content(
          schema = @Schema(implementation = UserResponse.class)
      )
  )
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserResponse>> findAll() {
    List<UserResponse> userResponses = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResponses);
  }

  @Operation(summary = "User 정보 수정", operationId = "update")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404",
          content = @Content(
              examples = @ExampleObject(value = "User with id {userId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(
              examples = @ExampleObject(value = "User with email {newEmail} already exists")
          )
      ),
      @ApiResponse(
          responseCode = "200",
          description = "User 정보가 성공적으로 수정됨",
          content = @Content(
              schema = @Schema(implementation = UserResponse.class)
          )
      )
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH,
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponse> update(
      @Parameter(description = "수정할 UserID", required = true)
      @PathVariable(value = "userId") UUID userId,
      @Parameter(description = "User 변경 요청 정보", required = true)
      @RequestPart(value = "userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @Parameter(description = "수정할 User 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.update(userId, userUpdateRequest, profile));
  }

  @Operation(summary = "User 온라인 상태 업데이트", operationId = "updateUserStatusByUserId")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404",
          description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "200",
          description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(
              schema = @Schema(implementation = UserStatusResponse.class)
          )
      )
  })
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatusResponse> updateUserState(
      @Parameter(description = "상태를 변경할 User ID", required = true)
      @PathVariable(value = "userId") UUID userId,
      @Parameter(description = "유저 온라인 상태 업데이트 요청", required = true)
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatusResponse userStatusResponse = userStatusService.updateUserStatusByUserId(userId,
        userStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusResponse);
  }

  @Operation(summary = "User 삭제", operationId = "delete")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "User가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(value = "User with id {id} not found")
          )
      )
  })
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 User ID")
      @PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
