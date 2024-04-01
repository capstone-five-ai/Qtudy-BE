package com.app.domain.problem.value;

import lombok.*;

@Getter
@AllArgsConstructor
public class S3FileInformation {
    private final String bucketName;
    private final String fileKey;
    private final String fileUrl;


}
