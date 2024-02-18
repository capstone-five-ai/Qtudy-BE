package com.app.gpt;

import com.app.domain.problem.service.ProblemFileService;
import com.app.gpt.dto.ProblemSchema;
import com.app.gpt.dto.SummarySchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.jakarta.factories.SchemaFactoryWrapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.app.gpt.dto.ProblemSchema.ProblemChoices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class GPTResponseFormatTest {
    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncodingForModel(ModelType.GPT_3_5_TURBO);
    private static final String GPT_3_5_TURBO_0125 = "gpt-3.5-turbo-0125";
    private static final String APPLICATION_LOCAL_YML = "src/main/resources/application-local.yml";

    private static final FunctionExecutor PROBLEM_FUNCTION_EXECUTOR = new FunctionExecutor(List.of(ChatFunction.builder()
                                                                                                               .name("get_new_quiz")
                                                                                                               .description("요청한 내용을 기반으로 객관식 문제 만들어줘 빈칸이 있으면 안돼")
                                                                                                               .executor(ProblemSchema.class, parameter -> parameter)
                                                                                                               .build()));

    private static final String DIFFICULTY = "어려운";
    private static final int MAX_TOKEN_COUNT = 550;

    private static final String TEST_PDF_FILE_PATH = "src/test/resources/pdf/";
    private static final String TEST_TEXT_FILE_PATH = "src/test/resources/text/";

    private OpenAiService openAiService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        Map<String, String> properties = new Yaml().load(new FileReader(APPLICATION_LOCAL_YML));
        String openAiApiToken = properties.get("openai-api-token");
        openAiService = new OpenAiService(openAiApiToken);

        objectMapper = new ObjectMapper();
    }

//    @RepeatedTest(5)
    @Test
    void 텍스트_기반_AI_문제_생성() throws IOException {
        String testFileName = "text1.text";
        String text = Files.readString(Paths.get(TEST_TEXT_FILE_PATH + testFileName));
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "당신이 시험 문제 출제자가 됐다고 가정하겠습니다."));

        List<String> textChunks = splitToken(text);
        for (String textChunk : textChunks) {
            String prompt = String.format("%s의 내용을 기반으로 %s 객관식 문제 만들어줘\"", textChunk, DIFFICULTY);
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

            ChatMessage responseMessage = createChatCompletion(PROBLEM_FUNCTION_EXECUTOR, messages);
            JsonNode arguments = responseMessage.getFunctionCall().getArguments();

            System.out.println(arguments.toPrettyString());
            assertProblemSchema(arguments);
        }
    }

//    @RepeatedTest(5)
    @Test
    void PDF_기반_AI_문제_생성() throws IOException {
        String testFileName = "pdf1.pdf";
        String text = readPdfFile(testFileName);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "당신이 시험 문제 출제자가 됐다고 가정하겠습니다."));

        List<String> textChunks = splitToken(text);
        for (String textChunk : textChunks) {
            String prompt = String.format("%s의 내용을 기반으로 %s 객관식 문제 만들어줘", textChunk, DIFFICULTY);
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

            ChatMessage responseMessage = createChatCompletion(PROBLEM_FUNCTION_EXECUTOR, messages);
            JsonNode arguments = responseMessage.getFunctionCall().getArguments();

            System.out.println(arguments.toPrettyString());
            assertProblemSchema(arguments);
        }
    }

    private List<String> splitToken(String text) {
        List<String> tokens = new ArrayList<>();

        StringBuilder currentText = new StringBuilder();
        for (char character : text.toCharArray()) {
            currentText.append(character);
            if (ENCODING.countTokens(currentText.toString()) >= MAX_TOKEN_COUNT) {
                tokens.add(currentText.toString());
                currentText.setLength(0);
            }
        }
        tokens.add(currentText.toString());
        return tokens;
    }

    private void assertProblemSchema(JsonNode arguments) throws JsonProcessingException {
        assertDoesNotThrow(() -> objectMapper.treeToValue(arguments, ProblemSchema.class), "예상치 못한 값이 Json 데이터에 포함되어 있습니다.");
        ProblemSchema problemSchema = objectMapper.treeToValue(arguments, ProblemSchema.class);
        ProblemChoices problemChoices = problemSchema.getProblemChoices();
        assertAll(
                () -> assertThat(problemSchema.getProblemName()).isNotNull(),
                () -> assertThat(problemChoices.getOne()).isNotNull(),
                () -> assertThat(problemChoices.getTwo()).isNotNull(),
                () -> assertThat(problemChoices.getThree()).isNotNull(),
                () -> assertThat(problemChoices.getFour()).isNotNull(),
                () -> assertThat(problemSchema.getProblemAnswer()).isNotNull(),
                () -> assertThat(problemSchema.getProblemCommentary()).isNotNull()
        );
    }

//    @RepeatedTest(5)
    @Test
    void PDF_기반_AI_요약정리_생성() throws IOException {
        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                                                                                     .name("get_new_summary")
                                                                                     .description("요청한 내용을 기반으로 요점 정리해줘")
                                                                                     .executor(SummarySchema.class, parameter -> parameter)
                                                                                     .build()));

        String testFileName = "pdf1.pdf";
        String text = readPdfFile(testFileName);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "당신은 강의 자료의 핵심 정보를 요약해주는 역할입니다."));

        List<String> textChunks = splitToken(text);
        for (String textChunk : textChunks) {
            String prompt = String.format("%s의 내용을 기반으로 요점 정리 해줘", textChunk);
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

            ChatMessage responseMessage = createChatCompletion(functionExecutor, messages);
            JsonNode arguments = responseMessage.getFunctionCall().getArguments();

            System.out.println(arguments.toPrettyString());
            assertDoesNotThrow(() -> objectMapper.treeToValue(arguments, SummarySchema.class), "예상치 못한 값이 Json 데이터에 포함되어 있습니다.");
            SummarySchema summarySchema = objectMapper.treeToValue(arguments, SummarySchema.class);
            assertThat(summarySchema).isNotNull();
        }
    }

    private String readPdfFile(String testFileName) throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(TEST_PDF_FILE_PATH + testFileName));
        return ProblemFileService.convertFileToString(multipartFile);
    }

    private ChatMessage createChatCompletion(FunctionExecutor functionExecutor, List<ChatMessage> messages) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                                                           .model(GPT_3_5_TURBO_0125)
                                                                           .messages(messages)
                                                                           .functions(functionExecutor.getFunctions())
                                                                           .functionCall(ChatCompletionRequestFunctionCall.of("auto"))
                                                                           .maxTokens(1_024)
                                                                           .build();

        return openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
    }

    @Test
    void 스키마를_JSON으로_변환하여_출력한다() throws JsonProcessingException {
        // 메타데이터가 포함되지 않은 JSON 출력
        ProblemSchema fp = new ProblemSchema(
                "문제명",
                new ProblemChoices(
                        "1번 선지",
                        "2번 선지",
                        "3번 선지",
                        "4번 선지"
                ),
                "문제의 답",
                "문제 해설"
        );
        String normal = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fp);
        System.out.println(normal);

        System.out.println("-----------");
        // 메타데이터가 포함된 JSON 출력
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        objectMapper.acceptJsonFormatVisitor(ProblemSchema.class, visitor);
        JsonSchema jsonSchema = visitor.finalSchema();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(jsonSchema);
        System.out.println(json);
    }
}
