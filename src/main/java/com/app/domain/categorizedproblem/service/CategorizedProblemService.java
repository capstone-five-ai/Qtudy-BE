package com.app.domain.categorizedproblem.service;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.categorizedproblem.repository.CategorizedProblemRepository;
import com.app.domain.category.entity.Category;
import com.app.domain.category.service.CategoryService;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.problem.aigeneratedproblem.service.AiGeneratedProblemService;
import com.app.domain.problem.entity.Problem;
import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
import com.app.domain.problem.membersavedproblem.mapper.MemberSavedProblemMapper;
import com.app.domain.problem.membersavedproblem.service.MemberSavedProblemService;
import com.app.domain.problem.service.ProblemService;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.global.config.ENUM.ProblemType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorizedProblemService {
    private final CategoryService categoryService;

    private final MemberSavedProblemService memberSavedProblemService;

    private final MemberService memberService;

    private final MemberSavedProblemMapper memberSavedProblemMapper;

    private final AiGeneratedProblemService aiGeneratedProblemService;

    private final CategorizedProblemRepository categorizedProblemRepository;

    private final ProblemService problemService;

    public CategorizedProblem createCategorizedProblem(Long categoryId, Long problemId) {
        checkForDuplicateCategorizedProblem(categoryId, problemId);

        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        CategorizedProblem categorizedProblem = createCategorizedProblemEntity(category, problemId);

        return categorizedProblemRepository.save(categorizedProblem);
    }

    /**
     * 카테고리화 문제의 카테고리에 이미 같은 문제가 저장되어있는지 확인
     * @param categoryId
     * @param problemId
     */
    private void checkForDuplicateCategorizedProblem(Long categoryId, Long problemId) {
        boolean exists = categorizedProblemRepository.existsByCategoryCategoryIdAndProblemProblemId(
                categoryId,
                problemId
        );

        if (exists) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORIZED_PROBLEM);
        }
    }

    private CategorizedProblem createCategorizedProblemEntity(Category category, Long problemId) {
        Problem problem = problemService.findVerifiedProblemByProblemId(problemId);
        CategorizedProblem categorizedProblem = CategorizedProblem.builder()
                .problem(problem)
                .build();
        categorizedProblem.updateCategory(category);
        return categorizedProblem;
    }

    public MemberSavedSummaryDto.pdfResponse createCategorizedProblemsAnswerPdf(Long categoryId) throws IOException {
        List<CategorizedProblem> categorizedProblemList = categorizedProblemRepository.findByCategoryCategoryId(categoryId);
        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        String title = category.getCategoryName();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/malgun.ttf"));
            contentStream.setFont(font, 12);
            float yPosition = page.getMediaBox().getHeight() - 50;
            int linesInCurrentPage = 0;
            int maxLinesPerPage = 45;
            int commentaryNumber = 0; // 해설 번호

            for (CategorizedProblem categorizedProblem : categorizedProblemList) {
                commentaryNumber++;
                String problemCommentary = commentaryNumber + ". " + categorizedProblem.getProblem().getProblemCommentary();

                float maxWidth = page.getMediaBox().getWidth() - 100; // 페이지 폭에서 양쪽 여백을 뺀 값
                List<String> wrappedProblemCommentary = wrapText(problemCommentary, font, 12, maxWidth);

                for (String line : wrappedProblemCommentary) {
                    if (linesInCurrentPage >= maxLinesPerPage || yPosition < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, 12);
                        yPosition = page.getMediaBox().getHeight() - 50;
                        linesInCurrentPage = 0;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= 15; // 줄 간의 간격
                    linesInCurrentPage++;
                }
                yPosition -= 20; // 해설 간의 간격 조정
            }
            contentStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream); // 문서를 ByteArrayOutputStream에 저장
            document.close(); // 문서를 닫아 리소스를 해제
            return new MemberSavedSummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), title);
        }
    }

    public MemberSavedSummaryDto.pdfResponse createCategorizedProblemsPdf(Long categoryId) throws IOException {
        List<CategorizedProblem> categorizedProblemList = categorizedProblemRepository.findByCategoryCategoryId(categoryId);
        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);
        String title = category.getCategoryName();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/malgun.ttf"));
            contentStream.setFont(font, 12);
            float yPosition = page.getMediaBox().getHeight() - 50;
            int linesInCurrentPage = 0;
            int maxLinesPerPage = 45;
            int problemNumber = 0; // 문제 번호

            for (CategorizedProblem categorizedProblem : categorizedProblemList) {
                problemNumber++;
                Problem problem = categorizedProblem.getProblem();
                String problemName = problemNumber + ". " + problem.getProblemName();

                List<String> problemChoices = new ArrayList<>();
                if (problem.getProblemType() == ProblemType.MULTIPLE) {
                    problemChoices = problem.getProblemChoices();
                }
                float maxWidth = page.getMediaBox().getWidth() - 100; // 페이지 폭에서 양쪽 여백을 뺀 값
                List<String> wrappedProblemName = wrapText(problemName, font, 12, maxWidth);

                for (String line : wrappedProblemName) {
                    if (linesInCurrentPage >= maxLinesPerPage || yPosition < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, 12);
                        yPosition = page.getMediaBox().getHeight() - 50;
                        linesInCurrentPage = 0;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= 15; // 줄 간의 간격
                    linesInCurrentPage++;
                }
                yPosition -= 10; // 문제명과 선택지 사이의 간격 조정

                // 보기 리스트 출력
                if (problemChoices != null) {
                    char choiceLetter = 'A'; // 선지 번호 (알파벳)
                    int totalChoices = problemChoices.size();

                    for (String choice : problemChoices) {
                        List<String> wrappedText = wrapText(choice, font, 12, maxWidth);
                        boolean isFirstLineOfChoice = true;

                        for (String line : wrappedText) {
                            if (linesInCurrentPage >= maxLinesPerPage || yPosition < 50) {
                                contentStream.close();
                                page = new PDPage();
                                document.addPage(page);
                                contentStream = new PDPageContentStream(document, page);
                                contentStream.setFont(font, 12);
                                yPosition = page.getMediaBox().getHeight() - 50;
                                linesInCurrentPage = 0;
                            }

                            contentStream.beginText();
                            contentStream.newLineAtOffset(50, yPosition);

                            if (isFirstLineOfChoice) {
                                contentStream.showText(choiceLetter + ". " + line); // 알파벳 번호 추가
                                isFirstLineOfChoice = false; // 첫 줄 출력 후 false로 변경
                            } else {
                                contentStream.showText(line);
                            }
                            contentStream.endText();
                            yPosition -= 15;
                            linesInCurrentPage++;
                        }
                        if (choiceLetter == 'A' + totalChoices - 1) { // 마지막 선지 확인
                            yPosition -= 20; // 마지막 선지 이후에 더 큰 간격 추가
                        }
                        choiceLetter++; // 다음 선지를 위해 알파벳 증가
                    }
                }
            }

            contentStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream); // 문서를 ByteArrayOutputStream에 저장
            document.close(); // 문서를 닫아 리소스를 해제
            return new MemberSavedSummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), title);
        }
    }



    public CategorizedProblem updateCategorizedProblem(Long categorizedProblemId, MemberSavedProblemDto.Patch problemPatchDto) {
        CategorizedProblem categorizedProblem = findVerifiedCategorizedProblemByCategorizedProblemId(categorizedProblemId);
        memberSavedProblemService.updateProblem(
                memberSavedProblemMapper.problemPatchDtoToProblem(problemPatchDto),
                categorizedProblem.getProblem().getProblemId()
        );
        return categorizedProblemRepository.save(categorizedProblem);
    }

    @Transactional(readOnly = true)
    public Page<CategorizedProblem> findCategorizedProblemsByCategoryId(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categorizedProblemRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }

    public void deleteCategorizedProblem(Long categorizedProblemID){
        CategorizedProblem categorizedProblem = findVerifiedCategorizedProblemByCategorizedProblemId(categorizedProblemID);
        categorizedProblemRepository.deleteById(categorizedProblemID);

        Problem problem = categorizedProblem.getProblem();
        Long problemId = problem.getProblemId();
        if (problem.isMemberSavedProblem() && !isProblemUsedInOtherCategorizedProblems(problemId)) {
            memberSavedProblemService.deleteProblem(problemId);
        }
    }

    private boolean isProblemUsedInOtherCategorizedProblems(Long problemId) {
        return categorizedProblemRepository.existsByProblemProblemId(problemId);
    }

    private List<String> wrapText(String text, PDType0Font font, float fontSize, float maxWidth) throws IOException {
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

    public CategorizedProblem findVerifiedCategorizedProblemByCategorizedProblemId(Long categorizedProblemId){
        return categorizedProblemRepository.findById(categorizedProblemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORIZED_PROBLEM_NOT_EXISTS));
    }

    public Boolean checkIsWriter(HttpServletRequest httpServletRequest, CategorizedProblem categorizedProblem) {
        Member member = memberService.getLoginMember(httpServletRequest);
        if (categorizedProblem.getCategory().getMember().getMemberId() == member.getMemberId()) {
            return true;
        }
        return false;
    }
}