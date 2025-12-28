package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.request.LoginRequest;
import com.sprint.mission.discodeit.DTO.request.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.request.UserUpdateRequest;
import com.sprint.mission.discodeit.DTO.response.UserResponse;
import com.sprint.mission.discodeit.FileUploadUtils;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final FileUploadUtils fileUploadUtils;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public UserResponse create(@RequestPart(value = "request") UserCreateRequest userCreateRequest,
                               @RequestPart(value = "file", required = false) MultipartFile file) {
        Optional<BinaryContentCreateRequest> binaryRequest = Optional.empty();

        if (file != null && !file.isEmpty()) {
            try {
                String uploadPath = fileUploadUtils.getUploadPath("images");
                String originalFileName = file.getOriginalFilename();
                String contentType = file.getContentType();
                byte[] bytes = file.getBytes();
                String ext = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    //확장자 추출
                    ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                }
                //Random으로 UUID 생성 후 -가 존재하는 경우 삭제 후 확장자와 결합
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                //saveName 이름과 uploadPath 경로를 가진 File객체 생성
                File targetFile = new File(uploadPath, savedName);

                //저장
                file.transferTo(targetFile);

                BinaryContentCreateRequest profile = new BinaryContentCreateRequest(originalFileName, savedName, uploadPath, contentType, bytes, null);
                binaryRequest = Optional.of(profile);

                System.out.println("파일 업로드 성공. 저장된 이름: " + savedName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userService.create(userCreateRequest, binaryRequest);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public UserResponse findById(@RequestParam(value = "id") UUID id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> userResponses = userService.findAll();
        return ResponseEntity.ok(userResponses);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public UserResponse update(@RequestParam(value = "id") UUID id,
                               @RequestPart(value = "request") UserUpdateRequest userUpdateRequest,
                               @RequestPart(value = "file", required = false) MultipartFile file) {
        Optional<BinaryContentCreateRequest> binaryRequest = Optional.empty();

        if (file != null && !file.isEmpty()) {
            try {
                String uploadPath = fileUploadUtils.getUploadPath("images");
                String originalFileName = file.getOriginalFilename();
                String contentType = file.getContentType();
                byte[] bytes = file.getBytes();
                String ext = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    //확장자 추출
                    ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                }
                //Random으로 UUID 생성 후 -가 존재하는 경우 삭제 후 확장자와 결합
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                //saveName 이름과 uploadPath 경로를 가진 File객체 생성
                File targetFile = new File(uploadPath, savedName);

                //저장
                file.transferTo(targetFile);

                BinaryContentCreateRequest profile = new BinaryContentCreateRequest(originalFileName, savedName, uploadPath, contentType, bytes, null);
                binaryRequest = Optional.of(profile);

                System.out.println("파일 업로드 성공. 저장된 이름: " + savedName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userService.update(id, userUpdateRequest, binaryRequest);
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
