package com.example.career.web;

import com.example.career.tools.CoverLetterTools;
import com.example.career.tools.GapAnalysisTools;
import com.example.career.tools.RewriteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final GapAnalysisTools gapAnalysisTools;
    private final RewriteTools rewriteTools;
    private final CoverLetterTools coverLetterTools;

    public ChatController(ChatClient careerChatClient, ChatMemory chatMemory,
                          GapAnalysisTools gapAnalysisTools, RewriteTools rewriteTools,
                          CoverLetterTools coverLetterTools) {
        this.chatClient = careerChatClient;
        this.chatMemory = chatMemory;
        this.gapAnalysisTools = gapAnalysisTools;
        this.rewriteTools = rewriteTools;
        this.coverLetterTools = coverLetterTools;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .tools(gapAnalysisTools, rewriteTools, coverLetterTools)
                .call()
                .content();

        return new ChatResponse(answer);
    }

    public record ChatRequest(String conversationId, String message) {}
    public record ChatResponse(String answer) {}
}