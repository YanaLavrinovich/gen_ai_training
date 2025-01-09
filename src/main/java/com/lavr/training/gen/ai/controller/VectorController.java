package com.lavr.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.lavr.training.gen.ai.dto.EmbeddingModelRequest;
import com.lavr.training.gen.ai.dto.EmbeddingModelResponse;
import com.lavr.training.gen.ai.service.VectorService;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
@Slf4j
public class VectorController {

  private final VectorService vectorService;

  @PostMapping(path = "/build")
  public ResponseEntity<List<EmbeddingItem>> getEmbedding(
      @RequestBody EmbeddingModelRequest request) {
    try {
      return ResponseEntity.ok(vectorService.getEmbeddings(request.getText()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(null);
    }
  }

  @PostMapping(path = "/save")
  public ResponseEntity<?> processAndSaveText(@RequestBody EmbeddingModelRequest request) {
    try {
      vectorService.saveEmbedding(request.getText());
      return ResponseEntity.noContent().build();
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error during processing and saving text");
      return ResponseEntity.internalServerError().body("Sorry, error during processing");
    }
  }

  @PostMapping(path = "/search")
  public ResponseEntity<List<EmbeddingModelResponse>> searchClosestEmbedding(
      @RequestBody EmbeddingModelRequest request) {
    try {
      return ResponseEntity.ok(vectorService.searchClosestEmbeddings(request.getText()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(null);
    }
  }
}
