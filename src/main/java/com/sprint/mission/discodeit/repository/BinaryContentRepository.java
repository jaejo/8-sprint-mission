package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.DTO.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

  BinaryContent save(BinaryContent binaryContent);

  List<BinaryContent> saveAll(List<BinaryContent> contents);

  Optional<BinaryContent> findById(UUID id);

  List<UUID> findAll();

  List<BinaryContent> findAllByIn(List<UUID> binaryContentIds);

  void delete(UUID id);
}
