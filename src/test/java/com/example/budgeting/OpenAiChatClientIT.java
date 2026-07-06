package com.example.budgeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatClientIT {
    @Autowired
    OpenAiChatModel openAiChatModel;

    @Test
    void should_executeSum_when_prompted() throws InterruptedException {
        var chatClient = ChatClient.builder(openAiChatModel).defaultSystem("Você é um matemático").build();

        var response = chatClient.prompt("Soma 10 mais 20. Depois subtraia 30 do resultado anterior.")
                .call().content();

        assertThat(response).contains("0");
        System.out.println(response);


    }

}
