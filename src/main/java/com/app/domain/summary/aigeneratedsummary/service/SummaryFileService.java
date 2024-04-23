package com.app.domain.summary.aigeneratedsummary.service;


import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.problem.aigeneratedproblem.value.S3FileInformation;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request.AiGenerateSummaryDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Response.AiGenerateSummaryResponseDto;
import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import com.app.domain.summary.aigeneratedsummary.entity.SummaryFile;
import com.app.domain.summary.aigeneratedsummary.repository.AiGeneratedSummaryRepository;
import com.app.domain.summary.aigeneratedsummary.repository.SummaryFileRepository;
import com.app.global.config.ENUM.*;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.pdf.SummaryPdfMaker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummaryFileService { //Service 추후 분할 예정


    private String base_url = "http://13.125.229.219:5000";;
  
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryFileRepository summaryFileRepository;
    @Autowired
    private AiGeneratedSummaryRepository aiGeneratedSummaryRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private S3Service s3Service;


    @Transactional
    public AiGenerateSummaryResponseDto AiGenerateSummaryFileByText(HttpServletRequest httpServletRequest, AiGenerateSummaryDto aiGenerateSummaryDto) {
        Member member = memberService.getLoginMember(httpServletRequest);
        String text = aiGenerateSummaryDto.getText();
        Amount amount = aiGenerateSummaryDto.getAmount();
        String fileName = aiGenerateSummaryDto.getFileName();

        String url = base_url + "/create/summary";;
        AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto;
        AiGeneratedSummary aiGeneratedSummary = null;

        if(summaryFileRepository.findByFileName(fileName).isPresent()){ // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }


        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryDto); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

        SummaryFile summaryFile = SaveSummaryFile(member, aiGenerateSummaryDto); //SUMMARY_FILE 테이블 저장
        aiGeneratedSummary = SaveSummarys(summaryFile, aiGenerateSummaryFromAiDto); // AI_GENERATED_SUMMARY 테이블 저장

        UploadS3(aiGenerateSummaryFromAiDto, aiGenerateSummaryDto, summaryFile.getFileId());

        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = AiGenerateSummaryResponseDto.ConvertToSummaryFileResponse(aiGeneratedSummary, summaryFile.getFileId());

        return aiGenerateSummaryResponseDto;
    }


    @Transactional
    public AiGenerateSummaryResponseDto AiGenerateSummaryFileByFile(HttpServletRequest httpServletRequest, List<MultipartFile> File, AiGenerateSummaryDto aiGenerateSummaryDto, FileType fileType) {
        Member member = memberService.getLoginMember(httpServletRequest);
        String url = base_url + "/create/summary";
        AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto;
        SummaryFile summaryFile;
        AiGeneratedSummary aiGeneratedSummary;


        switch (fileType){  //File 타입 체크
            case PDF: // PDF 타입일경우
                try {
                    String pdfText = convertFileToString(File.get(0));
                    String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

                    HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
                    jsonBody = objectMapper.writeValueAsString(aiGenerateSummaryDto.toTextDto(pdfText)); //HTTP BODY 생성 (FILE -> TEXTDto 변환)

                    HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                    aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴

                    summaryFile = SaveSummaryFile(member, aiGenerateSummaryDto); //SUMMARY_FILE 테이블 저장
                    aiGeneratedSummary = SaveSummarys(summaryFile, aiGenerateSummaryFromAiDto); // AI_GENERATED_SUMMARY 테이블 저장

                    UploadS3(aiGenerateSummaryFromAiDto, aiGenerateSummaryDto, summaryFile.getFileId());
                } catch (JsonProcessingException e) {
                    throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
                } catch (IOException e) {
                    throw new RuntimeException(e); // 추후 변경예정 (convertPdf)
                }
                break;

            case JPG: // JPG 타입일경우
                url+="/jpg";

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                for (MultipartFile file : File) {
                    //String encodedString = Base64.getEncoder().encodeToString(file.getBytes());
                    //map.add("files", encodedString); // 파일리스트 추가

                    try {
                        //파일 데이터를 바이트 배열로 읽어옴
                        byte[] fileBytes = file.getBytes();
                        //ByteArrayResource를 사용해 파일 데이터를 담은 자원 생성
                        ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                            @Override
                            public String getFilename() {
                                // 파일 원본 이름 반환
                                return file.getOriginalFilename();
                            }
                        };
                        //'files'라는 키로 ByteArrayResource를 추가
                        map.add("files", resource);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 예외 처리 로직 추가
                    }
                }

                // 다른 필드도 MultiValueMap에 추가
                map.add("amount", aiGenerateSummaryDto.getAmount().toString());

                HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers); // // HTTP 요청 전송
                aiGenerateSummaryFromAiDto = restTemplate.postForObject(url, request, AiGenerateSummaryFromAiDto.class); // http 응답 받아옴

                summaryFile = SaveSummaryFile(member, aiGenerateSummaryDto); //SUMMARY_FILE 테이블 저장
                aiGeneratedSummary = SaveSummarys(summaryFile, aiGenerateSummaryFromAiDto); // AI_GENERATED_SUMMARY 테이블 저장

                UploadS3(aiGenerateSummaryFromAiDto, aiGenerateSummaryDto, summaryFile.getFileId());
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = AiGenerateSummaryResponseDto.ConvertToSummaryFileResponse(aiGeneratedSummary, summaryFile.getFileId());


        return aiGenerateSummaryResponseDto;
    }


    public void UploadS3(AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto, AiGenerateSummaryDto aiGenerateSummaryDto, int fileId) {
        File tempFile = null;

        try { // 요점정리 PDF 생성
            String S3PdfName = fileId +"_SUMMARY.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = SummaryPdfMaker.CreatePdfFile(aiGenerateSummaryDto.getFileName(), aiGenerateSummaryFromAiDto,PdfType.SUMMARY); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(S3PdfName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);


        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }
    }




    // 사용자 입력 PDF를 String 문자열로 바꾸는 함수
    public String convertFileToString(MultipartFile pdfFile) throws IOException { // PDF파일을 String으로 변환
        try (InputStream is = pdfFile.getInputStream()) {
            PDDocument document = PDDocument.load(is);
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            document.close();
            return text;
        }
    }




    public SummaryFile SaveSummaryFile(Member member , AiGenerateSummaryDto aiGenerateSummaryDto){
        SummaryFile summaryFile = SummaryFile.builder()
                .member(member)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(aiGenerateSummaryDto.getFileName())
                .summaryAmount(aiGenerateSummaryDto.getAmount())
                .build();

        summaryFileRepository.save(summaryFile);
        return summaryFile;
    }

    public AiGeneratedSummary SaveSummarys (SummaryFile summaryFile, AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto){
        AiGeneratedSummary aiGeneratedSummary = AiGeneratedSummary.builder() // 문제생성
                .summaryFile(summaryFile)
                .summaryTitle(summaryFile.getFileName()) // 요점정리 제목 = 요점정리 파일 이름
                .summaryContent(aiGenerateSummaryFromAiDto.getSummaryContent())
                .build();

        aiGeneratedSummaryRepository.save(aiGeneratedSummary);

        return aiGeneratedSummary;
        }


    public Page<FileListResponseDto> allAiSummaryFileList(Pageable pageable, HttpServletRequest httpServletRequest){ //사용자가 생성한 모든 요점정리파일 리스트 가져오기
        Member member = memberService.getLoginMember(httpServletRequest);

        Page<SummaryFile> filePage = summaryFileRepository.findAllByMember(member,pageable); // 요점정리파일 이름 가져오기

        List<FileListResponseDto> fileListResponseDtoList = filePage.getContent().stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        DType.SUMMARY,
                        file.getCreateTime()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(fileListResponseDtoList, pageable, filePage.getTotalElements());
    }
}
