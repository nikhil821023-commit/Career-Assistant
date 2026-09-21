package com.example.career.web;

import com.example.career.service.ResumeIngestionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
public class ResumeUploadController {

    private final ResumeIngestionService ingestionService;

    public ResumeUploadController(ResumeIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        int chunks = ingestionService.ingest(file.getBytes(), file.getOriginalFilename());
        return new UploadResponse("Resume ingested: " + chunks + " chunks stored.");
    }

    public record UploadResponse(String message) {}
}