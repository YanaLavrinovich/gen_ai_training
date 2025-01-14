package com.lavr.training.gen.ai.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmbeddingStoreLoggingRetriever implements ContentRetriever {
  private final EmbeddingStoreContentRetriever retriever;

  @Override
  public List<Content> retrieve(Query query) {
    List<Content> relevant = retriever.retrieve(query);
    relevant.forEach(
        segment -> {
          log.info("=======================================================");
          log.info("Found relevant text segment: {}", segment.textSegment().text());
        });
    return relevant;
  }
}
