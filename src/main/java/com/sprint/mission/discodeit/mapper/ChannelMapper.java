package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.DTO.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
    MessageMapper.class,
    ReadStatusMapper.class,
    UserMapper.class})
public interface ChannelMapper {

  @Mapping(source = "participants", target = "participants")
  @Mapping(source = "lastMessageAt", target = "lastMessageAt")
  ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt);
}