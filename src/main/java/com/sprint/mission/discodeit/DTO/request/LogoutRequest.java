package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "로그아웃 정보")
public record LogoutRequest(
    UUID id
) {

}
