package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.LoginRequest;
import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public UserResponse create(@RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest, Optional.empty());
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public UserResponse findById(@RequestParam(value = "id") UUID id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public UserResponse update(@RequestParam(value = "id") UUID id,
                               @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(id, userUpdateRequest, Optional.empty());
    }

    @RequestMapping(value = "/update/{id}/online", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> updateOnline(@PathVariable(value = "id") UUID id) {
        UserResponse userResponse = userService.updateOnlineStatus(id);
        return ResponseEntity.ok(userResponse);
    }

    @RequestMapping(value = "/update/{id}/offline", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> updateOffline(@PathVariable(value = "id") UUID id) {
        UserResponse userResponse = userService.updateOfflineStatus(id);
        return ResponseEntity.ok(userResponse);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
