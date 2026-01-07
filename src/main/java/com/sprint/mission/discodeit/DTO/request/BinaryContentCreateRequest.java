package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "생성할 BinaryContent 정보")
public record BinaryContentCreateRequest(
    String originalFileName,
    String savedName,
    String uploadPath,
    String contentType,
    byte[] bytes,
    String description
) {

}
