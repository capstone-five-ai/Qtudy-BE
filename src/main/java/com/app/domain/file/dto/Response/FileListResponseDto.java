package com.app.domain.file.dto.Response;


import com.app.global.config.ENUM.DType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileListResponseDto {
    int fileId;
    String fileName;
    DType dtype;
    LocalDateTime createTime;
}
