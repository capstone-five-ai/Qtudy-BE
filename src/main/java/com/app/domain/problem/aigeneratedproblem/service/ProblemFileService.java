package com.app.domain.problem.aigeneratedproblem.service;


import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest.AiGenerateProblemFromAiDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemList;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemResponseDto;
import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.domain.problem.aigeneratedproblem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.aigeneratedproblem.repository.ProblemFileRepository;
import com.app.domain.problem.aigeneratedproblem.value.S3FileInformation;
import com.app.global.config.ENUM.*;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.app.global.pdf.ProblemPdfMaker.CreatePdfFile;

@Service
@Slf4j
public class ProblemFileService { //Service 추후 분할 예정


    private String base_url = "http://44.210.94.241:5000";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFileRepository problemFileRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AiGeneratedProblemRepository aiGeneratedProblemRepository;
    @Autowired
    private S3Service s3Service;


    @Transactional
    public AiGeneratedProblemResponseDto AiGenerateProblemFileByText(HttpServletRequest httpServletRequest, AiGenerateProblemDto aiGenerateProblemDto) {
        Member member = memberService.getLoginMember(httpServletRequest);
        String text = aiGenerateProblemDto.getText();
        ProblemType type = aiGenerateProblemDto.getType();
        Amount amount = aiGenerateProblemDto.getAmount();
        ProblemDifficulty difficulty = aiGenerateProblemDto.getDifficulty();
        String fileName = aiGenerateProblemDto.getFileName();

        List<AiGeneratedProblem> problems = new ArrayList<>();

        String url;
        AiGenerateProblemFromAiDto[] aiGenerateProblemFromAiDto;
        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

        if (problemFileRepository.findByFileName(fileName).isPresent()) { // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }

        switch (aiGenerateProblemDto.getType()) {  //Problem 타입 체크
            case MULTIPLE:
                url = base_url + "/create/problem/mcq";
                break;
            case SUBJECTIVE:
                url = base_url + "/create/problem/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
            jsonBody = objectMapper.writeValueAsString(aiGenerateProblemDto.toTextDto(text)); //HTTP BODY 생성

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
            aiGenerateProblemFromAiDto = restTemplate.postForObject(url, request, AiGenerateProblemFromAiDto[].class); // http 응답 받아옴
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

        ProblemFile problemFile = SaveProblemFile(member, aiGenerateProblemDto); //PROBLEM_FILE 테이블 저장
        problems = SaveProblems(problemFile, aiGenerateProblemFromAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

        UploadS3(aiGenerateProblemFromAiDto, aiGenerateProblemDto, problemFile.getFileId());

        List<AiGeneratedProblemList> ProblemList = problems.stream()
                .map(AiGeneratedProblemList::ConvertToProblem)
                .collect(Collectors.toList());

        AiGeneratedProblemResponseDto aiGeneratedProblemResponseDto = AiGeneratedProblemResponseDto.ConvertToProblem(ProblemList,problemFile.getFileId());

        return aiGeneratedProblemResponseDto;

    }


    @Transactional
    public AiGeneratedProblemResponseDto AiGenerateProblemFileByFile(HttpServletRequest httpServletRequest, List<MultipartFile> File, AiGenerateProblemDto aiGenerateProblemDto, FileType fileType) {
        Member member = memberService.getLoginMember(httpServletRequest);
        String url;
        AiGenerateProblemFromAiDto[] aiGenerateProblemFromAiDto;
        ProblemFile problemFile;
        List<AiGeneratedProblem> problems = new ArrayList<>();

        if (problemFileRepository.findByFileName(aiGenerateProblemDto.getFileName()).isPresent()) { // 이미 파일이름이 존재하는 경우 에러
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NAME);
        }


        switch (aiGenerateProblemDto.getType()) {  //Problem 타입 체크
            case MULTIPLE:
                url = base_url + "/create/problem/mcq";
                break;
            case SUBJECTIVE:
                url = base_url + "/create/problem/saq";
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        switch (fileType) {  //File 타입 체크
            case PDF: // PDF 타입일경우
                try {
                    String pdfText = convertFileToString(File.get(0));
                    String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환

                    HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성
                    jsonBody = objectMapper.writeValueAsString(aiGenerateProblemDto.toTextDto(pdfText)); //HTTP BODY 생성 (FILE -> TEXTDto 변환)

                    HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송
                    aiGenerateProblemFromAiDto = restTemplate.postForObject(url, request, AiGenerateProblemFromAiDto[].class); // http 응답 받아옴

                    problemFile = SaveProblemFile(member, aiGenerateProblemDto); //PROBLEM_FILE 테이블 저장
                    problems = SaveProblems(problemFile, aiGenerateProblemFromAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

                    UploadS3(aiGenerateProblemFromAiDto, aiGenerateProblemDto, problemFile.getFileId());// DTO 2개, FileId값 넘김

                } catch (JsonProcessingException e) {
                    throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
                } catch (IOException e) {
                    throw new RuntimeException(e); // 추후 변경예정 (convertPdf)
                }
                break;

            case JPG: // JPG 타입일경우
                url += "/jpg";

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
                map.add("type", aiGenerateProblemDto.getType().toString());  //그외 타입들 추가
                map.add("amount", aiGenerateProblemDto.getAmount().toString());
                map.add("difficulty", aiGenerateProblemDto.getDifficulty().toString());

                HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers); // // HTTP 요청 전송
                aiGenerateProblemFromAiDto = restTemplate.postForObject(url, request, AiGenerateProblemFromAiDto[].class); // http 응답 받아옴

                problemFile = SaveProblemFile(member, aiGenerateProblemDto); //PROBLEM_FILE 테이블 저장

                /*System.out.println(File.get(0).getResource());
                try {
                    System.out.println(Base64.getEncoder().encodeToString(File.get(0).getBytes()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("=== HTTP Request Information ===");
                System.out.println("URL: " + url);
                System.out.println("Method: POST");
                System.out.println("Headers: " + headers);*/
                System.out.println("Body: " + request.getBody().get("files"));


                problems = SaveProblems(problemFile, aiGenerateProblemFromAiDto); // AI_GENERATED_PROBLEMS 테이블 및 객관식 보기 저장

                UploadS3(aiGenerateProblemFromAiDto, aiGenerateProblemDto, problemFile.getFileId()); // DTO 2개, FileId값 넘김
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);
        }

        List<AiGeneratedProblemList> ProblemList = problems.stream()
                .map(AiGeneratedProblemList::ConvertToProblem)
                .collect(Collectors.toList());

        AiGeneratedProblemResponseDto aiGeneratedProblemResponseDto = AiGeneratedProblemResponseDto.ConvertToProblem(ProblemList,problemFile.getFileId());

        return aiGeneratedProblemResponseDto;


    }


    public void UploadS3(AiGenerateProblemFromAiDto[] aiGenerateProblemFromAiDto, AiGenerateProblemDto aiGenerateProblemDto , int fileId) {
        File tempFile = null;

        try { // 문제 PDF 생성
            String S3PdfName = fileId + "_PROBLEM.pdf";// S3에 저장되는 PDF이름
            tempFile = CreatePdfFile(aiGenerateProblemDto.getFileName(), aiGenerateProblemFromAiDto, PdfType.PROBLEM); // 파일 생성

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


        try { // 정답 PDF 생성
            String S3PdfName = fileId + "_ANSWER.pdf";// 예시로 앞글자를 사용한 .txt 파일 이름 생성 (추후 변경 예정)
            tempFile = CreatePdfFile(aiGenerateProblemDto.getFileName(), aiGenerateProblemFromAiDto, PdfType.ANSWER); // 파일 생성

            // 파일을 MultipartFile로 변환
            FileSystemResource resource = new FileSystemResource(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(S3PdfName, resource.getFilename(), "application/pdf", resource.getInputStream());

            //생성된 파일을 S3에 업로드
            S3FileInformation fileInfo = s3Service.uploadFile(multipartFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.NOT_UPLOAD_PROBLEM);
        } finally {
            if (tempFile != null) {
                tempFile.delete();  // 방금 생성한 파일 삭제
            }
        }
    }

    // 사용자 입력 PDF를 String 문자열로 바꾸는 함수
    public static String convertFileToString(MultipartFile pdfFile) throws IOException { // PDF파일을 String으로 변환
        try (InputStream is = pdfFile.getInputStream()) {
            PDDocument document = PDDocument.load(is);
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            document.close();
            return text;
        }
    }

    public ProblemFile SaveProblemFile(Member member, AiGenerateProblemDto aiGenerateProblemDto) {
        ProblemFile problemFile = ProblemFile.builder()
                .member(member)   //추후에 member 토큰으로 변경해야함.(추후 변경 예정)
                .fileName(aiGenerateProblemDto.getFileName()) //추후에 member가 지정한 이름으로 변경해야함.
                //.fileKey() // fileKey 삭제
                .problemDifficulty(aiGenerateProblemDto.getDifficulty())
                .problemAmount(aiGenerateProblemDto.getAmount())
                .problemType(aiGenerateProblemDto.getType())
                .build();

        problemFileRepository.save(problemFile); // 엔티티 저장 후 반환된 엔티티로 갱신


        return problemFile;
    }

    public List<AiGeneratedProblem> SaveProblems(ProblemFile problemFile, AiGenerateProblemFromAiDto[] aiGenerateProblemFromAiDtoArray) {
        List<AiGeneratedProblem> problems = new ArrayList<>();
        for (AiGenerateProblemFromAiDto aiDto : aiGenerateProblemFromAiDtoArray) {
            AiGeneratedProblem aiGeneratedProblem = AiGeneratedProblem.builder() // 문제생성
                    .problemFile(problemFile)
                    .problemName(aiDto.getProblemName())
                    .problemChoices(aiDto.getProblemChoices())
                    .problemAnswer(aiDto.getProblemAnswer())
                    .problemCommentary(aiDto.getProblemCommentary())
                    .problemType(problemFile.getProblemType())
                    .build();

            aiGeneratedProblemRepository.save(aiGeneratedProblem);
            problems.add(aiGeneratedProblem);
        }

        return problems;
    }


    public Page<FileListResponseDto> allAiProblemFileList(Pageable pageable, HttpServletRequest httpServletRequest) { //사용자가 생성한 모든 문제파일 리스트 가져오기
        Member member = memberService.getLoginMember(httpServletRequest);

        Page<ProblemFile> filePage = problemFileRepository.findAllByMemberOrderByCreateTimeDesc(member, pageable);

        List<FileListResponseDto> fileListResponseDtoList = filePage.getContent().stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        DType.PROBLEM,
                        file.getCreateTime(),
                        file.getUpdateTime()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(fileListResponseDtoList, pageable, filePage.getTotalElements());


    }
}
