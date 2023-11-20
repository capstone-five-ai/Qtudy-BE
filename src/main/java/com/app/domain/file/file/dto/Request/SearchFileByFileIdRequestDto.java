package com.app.domain.file.file.dto.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchFileByFileIdRequestDto {
    int fileId;
    String fileName;
}
