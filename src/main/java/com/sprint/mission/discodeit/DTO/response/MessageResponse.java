package com.sprint.mission.discodeit.DTO.response;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID uid,
        UUID cid,
        String channelName,
        String from,
        String content,
        List<UUID> attachmentIds

) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getUid(),
                message.getCid(),
                message.getChannelName(),
                message.getFrom(),
                message.getContent(),
                message.getAttachmentIds()
        );
    }
}
