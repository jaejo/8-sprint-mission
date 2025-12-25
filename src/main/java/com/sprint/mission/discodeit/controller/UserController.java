package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public UserResponse create(@RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest, Optional.empty());
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public UserResponse findById(@RequestParam(value = "id") UUID id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public UserResponse update(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(userUpdateRequest, Optional.empty());
    }

}
