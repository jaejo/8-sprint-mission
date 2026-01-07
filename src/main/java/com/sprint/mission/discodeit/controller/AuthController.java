package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.LoginRequest;
import com.sprint.mission.discodeit.DTO.request.LogoutRequest;
import com.sprint.mission.discodeit.DTO.response.LoginResponse;
import com.sprint.mission.discodeit.DTO.response.LogoutResponse;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로그인", operationId = "login")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "로그인 성공",
          content = @Content(
              schema = @Schema(
                  implementation = LoginResponse.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "사용자를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(value = "User with username {username} not found")
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "비밀번호가 일치하지 않음",
          content = @Content(
              examples = @ExampleObject(value = "Wrong password")
          )
      )
  })
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    UserResponse userResponse = authService.login(request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResponse);
  }

  @Operation(summary = "로그아웃", operationId = "logout")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "로그아웃 성공",
          content = @Content(
              schema = @Schema(
                  implementation = LogoutResponse.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "사용자가 이미 로그아웃 상태입니다.",
          content = @Content(
              examples = @ExampleObject(value = "User is already logged out.")
          )
      )
  })
  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
    String username = authService.logout(request.id());
    return ResponseEntity.ok(
        new LogoutResponse(username + " 님이 로그아웃했습니다."));
  }

}
