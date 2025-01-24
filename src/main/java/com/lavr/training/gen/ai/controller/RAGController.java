package com.lavr.training.gen.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lavr.training.gen.ai.service.RAGService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("openai/rag")
@RequiredArgsConstructor
public class RAGController {
    private final RAGService ragService;
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        try {
            return ResponseEntity.ok(ragService.ask(question));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Sorry, I can't process your question right now.");
        }
    }

    @PostMapping("document/upload")
    public ResponseEntity<?> uploadDocument(@RequestBody MultipartFile document) {
        try {
            ragService.uploadDocument(document);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Sorry, I can't process your question right now.");
        }
    }
}
