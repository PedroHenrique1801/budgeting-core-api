package com.example.budgeting;


import com.example.budgeting.application.GetFinancialSummaryUseCase;
import com.example.budgeting.application.SumTransactionsByCategoryUseCase;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BudgetingApplication {

    @Bean
    ChatClient chatChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public GetFinancialSummaryUseCase getFinancialSummaryUseCase(
            SumTransactionsByCategoryUseCase sumTransactionsByCategoryUseCase) {

        return new GetFinancialSummaryUseCase(sumTransactionsByCategoryUseCase);
    }

    public static void main(String[] args) {
        SpringApplication.run(BudgetingApplication.class, args);
    }
}