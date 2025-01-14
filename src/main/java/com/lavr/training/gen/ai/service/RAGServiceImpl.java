package com.lavr.training.gen.ai.service;

import java.io.IOException;
import java.io.InputStream;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class RAGServiceImpl implements RAGService {

  private final ConversationalRetrievalChain chain;
  private final EmbeddingStoreIngestor ingestor;
  private final DocumentParser documentParser;

  @Override
  public String ask(final String question) {
    log.info("Question: {}", question);
    String answer = chain.execute(question);
    log.info("Answer: {}", answer);
    return answer;
  }

  @Override
  public void uploadDocument(MultipartFile document) {
    try {
      InputStream inputStream = document.getInputStream();
      Document parsedDoc = documentParser.parse(inputStream);
      ingestor.ingest(parsedDoc);
    } catch (IOException e) {
      log.error("Error while uploading file", e);
      throw new RuntimeException(e);
    }
  }
}
