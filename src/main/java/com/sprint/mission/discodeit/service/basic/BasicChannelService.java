package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.dto.ChannelDto;
import com.sprint.mission.discodeit.DTO.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.DTO.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.DTO.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(name, description, ChannelType.PUBLIC);
    Channel savedChannel = channelRepository.save(channel);

    return this.toDto(savedChannel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    Channel savedChannel = channelRepository.save(channel);

    List<ReadStatus> readStatuses = request.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new NoSuchElementException(" 해당하는 유저가 존재하지 않습니다."));
          return new ReadStatus(user, channel, Instant.MIN);
        })
        .toList();

    //성능 향상을 위해 하나씩 저장하는 것보다 한번에 저장하는 로직
    readStatusRepository.saveAll(readStatuses);

    return this.toDto(savedChannel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 해당하는 채널이 존재하지 않습니다."));
  }

  @Override
  public List<ChannelDto> findAll(UUID userId) {
    List<Channel> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC) ||
                mySubscribedChannelIds.contains(channel)
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 해당하는 채널이 존재하지 않습니다."));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("개인 채널은 수정할 수 없습니다.");
    }

    channel.update(request.newName(), request.newDescription());

    return this.toDto(channelRepository.save(channel));
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 해당하는 채널이 존재하지 않습니다."));

    channelRepository.delete(channel);
  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            channel.getId())
        .map(Message::getCreatedAt)
        .orElse(Instant.MIN);

    List<User> participants = new ArrayList<>();

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(ReadStatus::getUser)
          .forEach(participants::add);
    }

    return channelMapper.toDto(channel, participants, lastMessageAt);
  }
}
