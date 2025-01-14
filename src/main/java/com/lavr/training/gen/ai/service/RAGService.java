package com.lavr.training.gen.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface RAGService {
    String ask(final String question);

    void uploadDocument(final MultipartFile document);
}
