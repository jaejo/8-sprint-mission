package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.response.BinaryContentResponse;
import com.sprint.mission.discodeit.FileStore;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final FileStore fileStore;

  @Override
  public BinaryContentResponse create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.originalFileName(),
        request.savedName(),
        request.uploadPath(),
        request.contentType(),
        request.bytes(),
        request.description()
    );

    binaryContentRepository.save(binaryContent);
    return BinaryContentResponse.from(binaryContent);
  }

  @Override
  public BinaryContentResponse find(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));

    return BinaryContentResponse.from(binaryContent);
  }

  @Override
  public List<BinaryContentResponse> findAllByIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIn(binaryContentIds).stream()
        .map(BinaryContentResponse::from)
        .toList();
  }

  public void delete(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));
    binaryContentRepository.delete(binaryContent.getId());
  }

  @Override
  public UUID save(MultipartFile file, String category) {
    if (file == null || file.isEmpty()) {
      return null;
    }

    BinaryContentCreateRequest binaryRequest = fileStore.storeFile(file, category);

    if (binaryRequest == null) {
      return null;
    }

    BinaryContent binaryContent = BinaryContent.from(binaryRequest);

    return binaryContentRepository.save(binaryContent).getId();
  }

  @Override
  public List<UUID> save(List<MultipartFile> files, String category) {
    if (files == null || files.isEmpty()) {
      return Collections.emptyList();
    }

    List<BinaryContentCreateRequest> binaryContentRequests = fileStore.storeFiles(files, category);

    List<BinaryContent> contents = binaryContentRequests.stream()
        .map(BinaryContent::from)
        .toList();

    List<BinaryContent> savedContents = binaryContentRepository.saveAll(contents);

    return savedContents.stream()
        .map(BinaryContent::getId)
        .toList();
  }
}
