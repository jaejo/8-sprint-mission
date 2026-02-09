package com.sprint.mission.discodeit.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel 생성 정보")
public record PrivateChannelCreateRequest(
    // 1. 리스트 자체가 null이거나 비어있으면 안 됨
    @NotEmpty(message = "At least one participant is required.")
    // 2. 리스트 내부의 각 요소(UUID)가 null이면 안 됨
    List<@NotNull(message = "Participant ID cannot be null.") UUID> participantIds
) {

}
