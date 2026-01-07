package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.request.MessageCreateRequest;
import com.sprint.mission.discodeit.DTO.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentService binaryContentService;

  @Override
  public MessageResponse create(MessageCreateRequest request, List<MultipartFile> files) {
    UUID userId = request.authorId();
    UUID channelId = request.channelId();

    userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("해당하는 유저를 찾을 수 없습니다."));
    channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당하는 채널을 찾을 수 없습니다."));

    List<UUID> attachmentIds = binaryContentService.save(files, "images");

    Message message = new Message(
        request.content(),
        request.channelId(),
        request.authorId(),
        attachmentIds
    );

    Message savedMessage = messageRepository.save(message);

    return MessageResponse.from(savedMessage);
  }

  @Override
  public MessageResponse findById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당하는 메시지를 찾을 수 없습니다."));

    return MessageResponse.from(message);
  }

  @Override
  public List<MessageResponse> findAllByChannelId(UUID id) {
    List<Message> messages = messageRepository.findAll();

    return messages.stream()
        .filter(message -> message.getChannelId().equals(id))
        .map(MessageResponse::from)
        .toList();
  }

  @Override
  public MessageResponse update(UUID id, MessageUpdateRequest request, List<MultipartFile> files) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("수정하려는 메시지가 없습니다."));

    message.update(request.content());

    return MessageResponse.from(message);
  }

  @Override
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("삭제하려는 메시지가 없습니다."));

    if (message.getAttachmentIds() != null) {
      message.getAttachmentIds().forEach(binaryContentRepository::delete);
    }
    messageRepository.delete(id);
  }
}
