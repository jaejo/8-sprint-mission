package com.sprint.mission.discodeit;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;

@Component
public class FileUploadUtils {

  @Value("${file.dir}")
  private String baseDir;

  public String getUploadPath(String subDirectory) {
    File directory = new File(baseDir, subDirectory);

    if (!directory.exists()) {
      System.out.println("파일 업로드를 위한 디렉토리 생성!");

      boolean created = directory.mkdirs();
      if (!created) {
        throw new RuntimeException("파일 업로드 디렉토리 생성 실패: " + directory.getAbsolutePath());
      }
    }

    return directory.getAbsolutePath();
  }
}
