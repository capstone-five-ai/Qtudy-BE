package com.app.global.pdf;

import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
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

public class SummaryPdfMaker {

    public static File CreatePdfFile(String fileName, AiGenerateSummaryFromAiDto aiGenerateSummaryFromAiDto, PdfType pdfType)  throws IOException { // String 기반으로 File 생성

        File tempFile = File.createTempFile("SUMMARY", ".pdf");

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


            List<String> wrappedSummaryName = wrapText(fileName, font, 13, maxWidth); // 요점정리명 나누기
            int summaryNameHeight = wrappedSummaryName.size() * 15; // 문제명의 높이 계산 (줄 수 * 줄 간격)

            contentStream.endText(); // 텍스트 모드 종료
            // 연두색 박스 그리기 시작
            PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
            graphicsState.setNonStrokingAlphaConstant(0.1f); // 투명도 설정
            contentStream.setGraphicsStateParameters(graphicsState);
            contentStream.setNonStrokingColor(0.78f, 1.0f, 0.78f); // 연두색 설정
            contentStream.addRect(20, yPosition - summaryNameHeight + 10, page.getMediaBox().getWidth() - 40, summaryNameHeight + 5); // 박스 위치 조정
            contentStream.fill();
            // 연두색 박스 그리기 종료

            contentStream.beginText(); // 텍스트 모드 시작
            // 텍스트 작업 시작
            graphicsState.setNonStrokingAlphaConstant(1f); // 투명도 재설정
            contentStream.setGraphicsStateParameters(graphicsState);
            contentStream.setNonStrokingColor(0f, 0f, 0f); // 검은색으로 설정
            contentStream.setFont(font, 13);
            contentStream.newLineAtOffset(30, yPosition);

            //요점정리명 작성
            for(String line : wrappedSummaryName) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
                yPosition -= 15; // 줄 간의 간격
            }

            contentStream.showText("");
            contentStream.newLineAtOffset(0, -15);
            yPosition -= 15; // 줄 간의 간격


            //요점정리내용 작성
            List<String> wrappedSummaryContent = wrapText(aiGenerateSummaryFromAiDto.getSummaryContent(), font,12, maxWidth);
            for(String line : wrappedSummaryContent) {
                if (linesInCurrentPage+5 >= maxLinesPerPage || yPosition < 70) {
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

                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
                yPosition -= 15; // 줄 간의 간격
            }

            contentStream.endText();
            contentStream.close();

            document.save(tempFile); // PDF 문서 저장
        }

        return tempFile;
    }

    private static List<String> wrapText(String text, PDType0Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            // 줄바꿈 문자 제거
            word = word.replaceAll("\\r|\\n", "");

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
