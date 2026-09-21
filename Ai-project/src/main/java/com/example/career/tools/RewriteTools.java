package com.example.career.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class RewriteTools {

    private final ChatClient chatClient;

    public RewriteTools(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Tool(description = "Rewrite a resume bullet point to be more impactful, " +
            "using strong action verbs and quantifiable results where possible")
    public String rewriteBullet(String bulletText, String tone) {
        return chatClient.prompt()
                .user("Rewrite this resume bullet in a " + tone + " tone, " +
                        "with a strong action verb and a quantifiable result if plausible: " +
                        bulletText)
                .call()
                .content();
    }
}