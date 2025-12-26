package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.MessageCreateRequest;
import com.sprint.mission.discodeit.DTO.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public MessageResponse create(@RequestBody MessageCreateRequest messageCreateRequest) {
        return messageService.create(messageCreateRequest, List.of());
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> findAllByChannelId(@RequestParam(value="id") UUID channelId){
        List<MessageResponse> messageResponses = messageService.findallByChannelId(channelId);
        return ResponseEntity.ok(messageResponses);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse> update(@RequestParam(value="id") UUID id,
                                                  @RequestBody MessageUpdateRequest messageUpdateRequest) {
        MessageResponse messageResponse = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.ok().build();
    }
}
