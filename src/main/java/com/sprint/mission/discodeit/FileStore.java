package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.Exception.FileUploadException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileStore {

  @Value("${file.dir}")
  private String fileDir;

  private final FileUploadUtils fileUploadUtils;

  public BinaryContentCreateRequest storeFile(MultipartFile file, String category) {
    if (file == null || file.isEmpty()) {
      return null;
    }

    List<BinaryContentCreateRequest> binaryRequests = storeFiles(List.of(file), category);

    return binaryRequests.isEmpty() ? null : binaryRequests.get(0);
  }

  public List<BinaryContentCreateRequest> storeFiles(List<MultipartFile> files, String category) {
    List<BinaryContentCreateRequest> binaryRequests = new ArrayList<>();
    //롤백용 리스트
    List<File> uploadedFiles = new ArrayList<>();

    if (files == null || files.isEmpty()) {
      return binaryRequests;
    }

    String fullPath = fileUploadUtils.getUploadPath(category);

    try {
      for (MultipartFile file : files) {
        if (file.isEmpty()) {
          continue;
        }
        String originalFileName = file.getOriginalFilename();
        String savedName = createStoreFileName(originalFileName);
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        // 파일 저장 로직
        File targetFile = new File(fullPath, savedName);
        file.transferTo(targetFile);
        uploadedFiles.add(targetFile);

        binaryRequests.add(new BinaryContentCreateRequest(
            originalFileName,
            savedName,
            fullPath,
            contentType,
            bytes,
            null
        ));
      }
    } catch (IOException e) {
      rollbackUploadedFiles(uploadedFiles);
      throw new FileUploadException();
    }

    return binaryRequests;
  }

  // 파일명 생성 로직 분리
  private String createStoreFileName(String originalFilename) {
    String ext = extractExt(originalFilename);
    return UUID.randomUUID().toString().replace("-", "") + "." + ext;
  }

  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }

  // 롤백 로직 분리
  private void rollbackUploadedFiles(List<File> files) {
    for (File file : files) {
      if (file.exists()) {
        file.delete();
      }
    }
  }
}
