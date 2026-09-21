package com.example.career.tools;

import com.example.career.model.GapAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class GapAnalysisTools {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public GapAnalysisTools(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    @Tool(description = "Compare the candidate's resume against a job description. " +
            "Returns matched skills, missing skills, and a fit summary.")
    public GapAnalysis analyzeGaps(String jobDescription) {
        return chatClient.prompt()
                .user("Compare the resume to this job description and identify " +
                        "matched skills, missing skills, and overall fit: " + jobDescription)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(5).build())
                        .build())
                .call()
                .entity(GapAnalysis.class);
    }
}