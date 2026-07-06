package com.example.budgeting.infrastructure.http;

import com.example.budgeting.application.GetFinancialSummaryUseCase;
import com.example.budgeting.application.ListTransactionsByCategoryUseCase;
import com.example.budgeting.application.PersistTransactionUseCase;
import com.example.budgeting.application.SumTransactionsByCategoryUseCase;
import com.example.budgeting.domain.Category;
import com.example.budgeting.infrastructure.http.request.TransactionRequest;
import com.example.budgeting.infrastructure.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;
    private final SumTransactionsByCategoryUseCase sumTransactionsByCategoryUseCase;
    private final GetFinancialSummaryUseCase financialSummaryUseCase;
    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
                                 SumTransactionsByCategoryUseCase sumTransactionsByCategoryUseCase,
                                 GetFinancialSummaryUseCase financialSummaryUseCase,
                                 TranscriptionModel transcriptionModel,
                                 @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                 ChatClient.Builder chatClientBuilder,
                                 TextToSpeechModel textToSpeechModel) throws IOException {

        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
        this.sumTransactionsByCategoryUseCase = sumTransactionsByCategoryUseCase;

        this.financialSummaryUseCase = financialSummaryUseCase;

        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, listTransactionsByCategoryUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping("/advisor")
    public Map<String, String> askAdvisor(@RequestBody Map<String, String> payload) {
        String userQuestion = payload.get("question");

        Map<String, Long> summary = financialSummaryUseCase.execute();

        double autoTotal = summary.getOrDefault("AUTO", 0L) / 100.0;
        double groceriesTotal = summary.getOrDefault("GROCERIES", 0L) / 100.0;
        double pharmaTotal = summary.getOrDefault("PHARMA", 0L) / 100.0;

        String systemPrompt = """
            Você é um consultor financeiro pessoal proativo, inteligente e focado em economia.
            Abaixo estão os gastos reais acumulados pelo usuário neste mês:
            - Categoria Carro (AUTO): R$ %.2f
            - Categoria Mercado (GROCERIES): R$ %.2f
            - Categoria Farmácia (PHARMA): R$ %.2f
            
            Use esses dados estritos para responder à pergunta do usuário de forma personalizada, 
            dando conselhos úteis, alertando se ele estiver gastando muito em uma categoria específica 
            e sendo direto na resposta. Formate os valores monetários em Reais (R$).
            """.formatted(autoTotal, groceriesTotal, pharmaTotal);

        String aiResponse = chatClient.prompt()
                .system(systemPrompt)
                .user(userQuestion)
                .call()
                .content();

        return Map.of("advice", aiResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category) {
        return listTransactionsByCategoryUseCase.execute(category)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @GetMapping("/sum/{category}")
    public Map<String, Object> getTransactionSumByCategory(@PathVariable Category category) {
        Long total = sumTransactionsByCategoryUseCase.execute(category);
        return Map.of(
                "category", category,
                "totalAmount", total
        );
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        var userMessage = transcriptionModel.transcribe(file.getResource());
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = textToSpeechModel.call(result);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }
}