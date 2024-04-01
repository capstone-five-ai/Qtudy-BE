package com.app.global.pdf;

import com.app.domain.problem.dto.ProblemFile.AiRequest.AiGenerateProblemFromAiDto;
import com.app.global.config.ENUM.PdfType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.jboss.jandex.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProblemPdfMaker {


    //PDF 파일 생성함수
    public static File CreatePdfFile(String fileName, AiGenerateProblemFromAiDto[] aiGenerateProblemFromAiDtoArray, PdfType pdfType) throws IOException { // String 기반으로 File 생성

        if (pdfType == PdfType.PROBLEM) { //문제 PDF 생성

            File tempFile = File.createTempFile(fileName, ".pdf");

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                int maxWidth = (int) (page.getMediaBox().getWidth() - 100); // 가로 최대값
                float yPosition = page.getMediaBox().getHeight() - 50; // 세로 최대값


                // 1페이지 및 글꼴 설정
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                PDType0Font font = PDType0Font.load(document, Main.class.getResourceAsStream("/fonts/malgun.ttf"));
                contentStream.newLineAtOffset(10, 700);

                int linesInCurrentPage = 0;
                int maxLinesPerPage = 42;

                // 한 문제마다 반복
                for (AiGenerateProblemFromAiDto problem : aiGenerateProblemFromAiDtoArray) {

                    List<String> wrappedProblemName = wrapText(problem.getProblemName(), font, 13, maxWidth); // 문제명 나누기
                    int problemNameHeight = wrappedProblemName.size() * 15; // 문제명의 높이 계산 (줄 수 * 줄 간격)

                    if (linesInCurrentPage+5 >= maxLinesPerPage || yPosition < 50) {
                        // 현재 페이지의 줄 수가 기준을 넘으면 새 페이지 추가
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage();
                        document.addPage(page);

                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(font, 12);
                        contentStream.newLineAtOffset(10, 700);

                        linesInCurrentPage = 0;
                        yPosition = page.getMediaBox().getHeight() - 50; // yPosition 초기화
                    }


                    contentStream.endText(); // 텍스트 모드 종료
                    // 연두색 박스 그리기 시작
                    PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                    graphicsState.setNonStrokingAlphaConstant(0.1f); // 투명도 설정
                    contentStream.setGraphicsStateParameters(graphicsState);
                    contentStream.setNonStrokingColor(0.78f, 1.0f, 0.78f); // 연두색 설정
                    contentStream.addRect(20, yPosition - problemNameHeight + 10, page.getMediaBox().getWidth() - 40, problemNameHeight + 5); // 박스 위치 조정
                    contentStream.fill();
                    // 연두색 박스 그리기 종료

                    contentStream.beginText(); // 텍스트 모드 시작
                    // 텍스트 작업 시작
                    graphicsState.setNonStrokingAlphaConstant(1f); // 투명도 재설정
                    contentStream.setGraphicsStateParameters(graphicsState);
                    contentStream.setNonStrokingColor(0f, 0f, 0f); // 검은색으로 설정
                    contentStream.setFont(font, 13);
                    contentStream.newLineAtOffset(30, yPosition);


                    // 문제명 작성
                    for(String line : wrappedProblemName){
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -15); // 12는 폰트 크기에 따라 조절
                        yPosition -= 15; // 줄 간의 간격
                    }
                    // 문제명, 보기 사이 여백 추가
                    contentStream.newLineAtOffset(0, -5);
                    yPosition -= 5;


                    // 문제보기 작성 (주관식인 경우 pass)
                    if (problem.getProblemChoices() != null) {
                        for (int i = 0; i < problem.getProblemChoices().size(); i++) {
                            List<String> wrappedProblemChoice = wrapText(problem.getProblemChoices().get(i), font, 12, maxWidth);
                            contentStream.showText(i + 1 + ". ");
                            for (String line : wrappedProblemChoice) {
                                contentStream.showText(line);
                                contentStream.newLineAtOffset(0, -15);
                                yPosition -= 15; // 줄 간의 간격
                            }
                        }
                        contentStream.showText("");
                        contentStream.newLineAtOffset(0, -15);
                        yPosition -= 15; // 줄 간의 간격

                        linesInCurrentPage++; // 현재 페이지 라인 수 증가
                    } else { // 주관식인 경우엔 빈칸만 추가
                        contentStream.showText("");
                        contentStream.newLineAtOffset(0, -15);
                        yPosition -= 15; // 줄 간의 간격
                    }
                }

                contentStream.endText();
                contentStream.close();
            }

            return tempFile;

        } else if (pdfType == PdfType.ANSWER) { // 정답 PDF 생성
            File tempFile = File.createTempFile(fileName, ".pdf");

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                int maxWidth = (int) (page.getMediaBox().getWidth() - 100); // 가로 최대값
                float yPosition = page.getMediaBox().getHeight() - 50; // 세로 최대값


                // 1페이지 및 글꼴 설정
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                PDType0Font font = PDType0Font.load(document, Main.class.getResourceAsStream("/fonts/malgun.ttf"));
                contentStream.newLineAtOffset(10, 700);

                int linesInCurrentPage = 0;
                int maxLinesPerPage = 42;

                // 한 문제마다 반복
                for (AiGenerateProblemFromAiDto problem : aiGenerateProblemFromAiDtoArray) {

                    List<String> wrappedProblemName = wrapText(problem.getProblemName(), font, 13, maxWidth); // 문제명 나누기
                    int problemNameHeight = wrappedProblemName.size() * 15; // 문제명의 높이 계산 (줄 수 * 줄 간격)

                    if (linesInCurrentPage+5 >= maxLinesPerPage || yPosition < 50) {
                        // 현재 페이지의 줄 수가 기준을 넘으면 새 페이지 추가
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage();
                        document.addPage(page);

                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(font, 12);
                        contentStream.newLineAtOffset(10, 700);

                        linesInCurrentPage = 0;
                        yPosition = page.getMediaBox().getHeight() - 50; // yPosition 초기화
                    }


                    contentStream.endText(); // 텍스트 모드 종료
                    // 연두색 박스 그리기 시작
                    PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                    graphicsState.setNonStrokingAlphaConstant(0.1f); // 투명도 설정
                    contentStream.setGraphicsStateParameters(graphicsState);
                    contentStream.setNonStrokingColor(0.78f, 1.0f, 0.78f); // 연두색 설정
                    contentStream.addRect(20, yPosition - problemNameHeight + 10, page.getMediaBox().getWidth() - 40, problemNameHeight + 5); // 박스 위치 조정
                    contentStream.fill();
                    // 연두색 박스 그리기 종료

                    contentStream.beginText(); // 텍스트 모드 시작
                    // 텍스트 작업 시작
                    graphicsState.setNonStrokingAlphaConstant(1f); // 투명도 재설정
                    contentStream.setGraphicsStateParameters(graphicsState);
                    contentStream.setNonStrokingColor(0f, 0f, 0f); // 검은색으로 설정
                    contentStream.setFont(font, 13);
                    contentStream.newLineAtOffset(30, yPosition);


                    // 문제명 작성
                    for(String line : wrappedProblemName){
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -15); // 12는 폰트 크기에 따라 조절
                        yPosition -= 15; // 줄 간의 간격
                    }
                    // 문제명, 보기 사이 여백 추가
                    contentStream.newLineAtOffset(0, -5);
                    yPosition -= 5;


                    // 문제 정답 작성 (주관식인 경우 pass)
                    contentStream.showText("정답 : " + problem.getProblemAnswer());
                    contentStream.newLineAtOffset(0, -15);

                    // 문제 해설 작성
                    List<String> wrappedProblemCommentary = wrapText(problem.getProblemCommentary(), font,12, maxWidth);
                    contentStream.showText("해설 : ");
                    yPosition -= 15;
                    for(String line : wrappedProblemCommentary){
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0,-15);
                        yPosition -= 15;
                    }

                    contentStream.showText("");
                    contentStream.newLineAtOffset(0, -15);
                    yPosition -= 15;
                    contentStream.showText("");
                    contentStream.newLineAtOffset(0, -15);
                    yPosition -= 15; // 문제 정답 pdf 의 경우는 이렇게 두개를 해야 줄 한칸이 띄어짐. (이유를 모르겠음;;)

                    linesInCurrentPage++; // 현재 페이지 라인 수 증가
                }

                contentStream.endText();
                contentStream.close();
            }

            return tempFile;
        }
        return null;
    }


    private static List<String> wrapText(String text, PDType0Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (font.getStringWidth(line + word) / 1000 * fontSize > maxWidth) {
                lines.add(line.toString().trim());
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }

        if (line.length() > 0) {
            lines.add(line.toString().trim());
        }

        return lines;
    }



}
