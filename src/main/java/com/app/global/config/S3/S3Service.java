package com.app.global.config.S3;

import com.app.domain.problem.value.S3FileInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service  // 스프링의 Service 빈으로 등록
public class S3Service {

    @Autowired  // S3Client 객체를 자동으로 주입
    private S3Client s3Client;

    // 사용할 S3 버킷 이름
    private static final String BUCKET_NAME = "q-study-bucket";

    /**
     * S3에 파일을 업로드하는 메서드
     *
     * @param file 스프링의 MultipartFile 객체, 업로드할 파일
     */
    public S3FileInformation uploadFile(MultipartFile file) throws IOException{

        String fileName = file.getName();  // 사용자가 요청한 이름으로 파일명 작성

            // S3에 업로드할 객체의 정보를 설정
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME) // 버킷 이름
                .key(fileName) // 파일 이름
                .build();

        // 설정된 S3클라이언트에 파일을 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize())); // 파일 내용,크기 기반 업로드
        String fileUrl = "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
        return new S3FileInformation(BUCKET_NAME, fileName, fileUrl);
    }




    /**
     * S3에서 파일을 다운로드하는 메서드
     *
     * @param fileKey 다운로드할 파일의 이름
     * @return 파일의 바이트 배열
     */
    public byte[] downloadFile(String fileKey) {
        try (ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(GetObjectRequest.builder() // 파일 다운로드
                .bucket(BUCKET_NAME) // 버킷 이름
                .key(fileKey) // 파일 이름
                .build());
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 스트림에서 파일 내용을 바이트 배열로 변환
            byte[] buffer = new byte[1024];
            int length;
            while ((length = objectData.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 다운로드 중 에러 발생", e);
        }
    }




    /**
     * S3 버킷의 모든 파일 목록을 조회하는 메서드
     *
     * @return 파일 이름의 리스트
     */
    public Optional<List<String>> listFiles(String token) {
        ListObjectsResponse listObjectsResponse = s3Client.listObjects(ListObjectsRequest.builder() // 파일 리스트 받아오기
                .bucket(BUCKET_NAME)
                .build());

        // 객체 목록에서 파일 이름만 추출하여 반환
        List<String> fileList = listObjectsResponse.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.contains(token))
                .collect(Collectors.toList());

        return Optional.ofNullable(fileList.isEmpty() ? null : fileList); // 아무것도 없으면 null 반환
    }





    /**
     * S3에서 특정 파일을 삭제하는 메서드
     *
     * @param fileName 삭제할 파일의 이름
     */
    public void deleteFile(String fileName) {
        s3Client.deleteObject(DeleteObjectRequest.builder() // 파일 삭제
                .bucket(BUCKET_NAME) //버킷 이름
                .key(fileName) // 파일 이름
                .build());
    }
}
