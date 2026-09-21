package com.example.career.tools;

import com.example.career.model.GapAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GapAnalysisTools {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public GapAnalysisTools(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;

        this.chatClient = builder
                .defaultSystem("""
                        ROLE: You are a career assistant that analyzes a
                        candidate's resume against a job description.

                        RULES:
                        - Analyze only genuine, realistic job descriptions.
                        - If the job description is nonsensical, absurd,
                          fictional, or contains impossible requirements,
                          do not interpret it literally or metaphorically.
                        - For an invalid job description, clearly state that
                          you cannot perform the analysis and ask the user
                          to provide a real job description.
                        - Do not invent job requirements.
                        - Base your analysis on the candidate's resume and
                          the provided job description.
                        - Identify matched skills, missing skills, and provide
                          a concise fit summary.
                        """)
                .build();
    }

    @Tool(description = "Compare the candidate's resume against a job description. " +
            "Returns matched skills, missing skills, and a fit summary.")
    public GapAnalysis analyzeGaps(String jobDescription) {
        try {
            return chatClient.prompt()
                    .user("Compare the resume to this job description and identify " +
                            "matched skills, missing skills, and overall fit: " + jobDescription)
                    .advisors(new QuestionAnswerAdvisor(vectorStore))
                    .call()
                    .entity(GapAnalysis.class);
        } catch (Exception e) {
            return new GapAnalysis(
                    List.of(),
                    List.of(),
                    "I had trouble analyzing this — could you try rephrasing the job description?"
            );
        }
    }
}