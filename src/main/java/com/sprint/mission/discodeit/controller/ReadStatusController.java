package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.DTO.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.Locked.Read;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "Message 읽음 상태 생성", operationId = "create_1")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(value = "Channel | User with id {channelId | userId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "이미 읽음 상태가 존재함",
          content = @Content(
              examples = @ExampleObject(value = "ReadStatus with userId {userId} and channelId {channelId} already exists")
          )
      ),
      @ApiResponse(
          responseCode = "201",
          description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(
                  implementation = ReadStatusResponse.class
              )
          )
      )
  })
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ReadStatusResponse> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusService.create(readStatusCreateRequest));
  }

  @Operation(summary = "User의 Message 읽음 상태 목록 조회", operationId = "findAllByUserId")
  @ApiResponse(
      responseCode = "200",
      description = "Message 읽음 상태 목록 조회 성공",
      content = @Content(
          schema = @Schema(implementation = ReadStatusResponse.class)
      )
  )
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusResponse>> find(
      @Parameter(description = "조회할 User ID", required = true)
      @RequestParam(value = "userId") UUID userId) {
    List<ReadStatusResponse> readStatusResponses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusResponses);
  }

  @RequestMapping(value = "/update", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusResponse> update(@RequestParam(value = "userId") UUID userId,
      @RequestParam(value = "channelId") UUID channelId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusResponse readStatusResponse = readStatusService.update(userId, channelId,
        readStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusResponse);
  }

  @Operation(summary = "Message 읽음 상태 수정", operationId = "update_1")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(
              schema = @Schema(implementation = ReadStatusResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(value = "ReadStatus with id {readStatusId} not found")
          )
      )
  })
  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusResponse> updateById(
      @Parameter(description = "수정할 읽음 상태 ID", required = true)
      @PathVariable(value = "readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusResponse readStatusResponse = readStatusService.update(readStatusId,
        readStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusResponse);
  }
}
