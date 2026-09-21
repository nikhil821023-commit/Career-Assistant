package com.example.career.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class CoverLetterTools {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public CoverLetterTools(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    @Tool(description = "Draft a tailored cover letter using the candidate's resume " +
            "content and the target job description")
    public String draftCoverLetter(String jobDescription) {
        return chatClient.prompt()
                .user("Write a concise, tailored cover letter for this job description, " +
                        "using relevant resume experience: " + jobDescription)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(5).build())
                        .build())
                .call()
                .content();
    }
}