package com.app.domain.file.file.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchFileByFileIdDto {
    int fileId;
    String fileName;
}
