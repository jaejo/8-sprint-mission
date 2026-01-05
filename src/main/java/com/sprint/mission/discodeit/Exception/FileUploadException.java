package com.sprint.mission.discodeit.Exception;

public class FileUploadException extends RuntimeException {

  public FileUploadException() {
    super("파일 업로드 중 에러가 발생하였습니다.");
  }
}
