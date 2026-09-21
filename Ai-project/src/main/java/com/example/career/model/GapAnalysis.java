package com.example.career.model;

import java.util.List;

public record GapAnalysis (
    List<String> matchedSkills,
    List<String> missingSkills,
    String fitSummary)
{}
