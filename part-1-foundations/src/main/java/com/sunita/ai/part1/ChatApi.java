package com.sunita.ai.part1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
class ChatApi {
    private final ChatClient chatClient;
    private final OrderTools orderTools;
    private final OrderStatusService orderStatusService;

    ChatApi(ChatClient chatClient, OrderTools orderTools, OrderStatusService orderStatusService) {
        this.chatClient = chatClient;
        this.orderTools = orderTools;
        this.orderStatusService = orderStatusService;
    }

    // General chat: no tool schema is sent, so input-token usage is lower.
    @PostMapping("/chat")
    ChatReply chat(@Valid @RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .system("Answer in at most three short sentences.")
                .user(request.message())
                .call()
                .content();
        return new ChatReply(answer);
    }

    // Natural-language order chat: tools are attached only when required.
    @PostMapping("/orders/chat")
    ChatReply orderChat(@Valid @RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .system("Use the order tool. Return only order ID, status, and a short next action.")
                .user(request.message())
                .tools(orderTools)
                .call()
                .content();
        return new ChatReply(answer);
    }

    // Deterministic lookup: bypasses the LLM and therefore consumes zero model tokens.
    @GetMapping("/orders/{orderId}/status")
    ResponseEntity<OrderStatus> orderStatus(@PathVariable String orderId) {
        return orderStatusService.findStatus(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/incidents/classify")
    IncidentClassification classify(@Valid @RequestBody IncidentRequest request) {
        IncidentClassification result = chatClient.prompt()
                .system("""
                        Classify the incident. Category: PAYMENT, ORDER, SECURITY, or OTHER.
                        Severity: LOW, MEDIUM, or HIGH. Summary: maximum 15 words.
                        Confidence: number from 0 to 1.
                        """)
                .user(request.description())
                .call()
                .entity(IncidentClassification.class);
        if (result.confidence() < 0 || result.confidence() > 1) {
            throw new IllegalStateException("Invalid model confidence");
        }
        return result;
    }
}

record ChatRequest(@NotBlank @Size(max = 2000) String message) {}
record ChatReply(String answer) {}
record IncidentRequest(@NotBlank @Size(max = 2000) String description) {}
record IncidentClassification(String category, String severity, String summary, double confidence) {}
