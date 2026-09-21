package com.example.career;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CareerAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerAssistantApplication.class, args);
    }

    @Bean
    ChatClient careerChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                    ROLE: You are a career assistant helping a candidate improve
                    their resume and apply for jobs.

                    RULES:
                    - Use the candidate's resume content (given as context) when
                      analyzing fit, rewriting bullets, or drafting letters.
                    - When asked to analyze fit against a job description, use
                      the analyzeGaps tool.
                    - When asked to improve a bullet point, use the rewriteBullet tool.
                    - When asked for a cover letter, use the draftCoverLetter tool.
                    - Be concise and practical. No filler.
                    """)
                .build();
    }
}