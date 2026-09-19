package com.sunita.ai.part1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AiConfiguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("""
                You are an operations assistant. Be concise.
                Never invent facts. Say when information is unavailable.
                """).build();
    }
}
