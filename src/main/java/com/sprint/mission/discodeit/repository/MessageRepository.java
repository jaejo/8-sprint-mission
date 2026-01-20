package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  //N+1 문제 발생 - message를 조회할 때 User와 BinaryContent가 최대 50번씩 실행될 수 있으므로
  @EntityGraph(attributePaths = {"author", "author.profile"})
  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  //커서 페이지네이션
  @EntityGraph(attributePaths = {"author", "author.profile"})
  @Query("""
          SELECT m FROM Message m
          WHERE m.channel.id = :channelId
            AND m.createdAt < :cursor
          ORDER BY m.createdAt DESC
      """)
  Slice<Message> findAllByCursor(
      @Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor,
      Pageable pageable
  );

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
