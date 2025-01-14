package com.lavr.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIClient;
import com.lavr.training.gen.ai.service.EmbeddingStoreLoggingRetriever;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LangChainConfiguration {
  @Bean
  public EmbeddingModel embeddingModel() {
    return new AllMiniLmL6V2EmbeddingModel();
  }

  @Bean
  public EmbeddingStore<TextSegment> embeddingStore() {
    return new InMemoryEmbeddingStore<>();
  }

  @Bean
  public DocumentParser documentParser() {
    return new TextDocumentParser();
  }

  @Bean
  public EmbeddingStoreContentRetriever contentRetriever(
      EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
    return EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(5)
        .minScore(0.7)
        .build();
  }

  @Bean
  public EmbeddingStoreIngestor ingestor(
      final EmbeddingModel embeddingModel, final EmbeddingStore<TextSegment> embeddingStore) {
    return EmbeddingStoreIngestor.builder()
        .documentSplitter(DocumentSplitters.recursive(500, 100))
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .build();
  }

  @Bean
  public EmbeddingStoreLoggingRetriever storeLoggingRetriever(
      EmbeddingStoreContentRetriever contentRetriever) {
    return new EmbeddingStoreLoggingRetriever(contentRetriever);
  }

  @Bean
  public ConversationalRetrievalChain chain(
      final OpenAIClient openAIClient, final EmbeddingStoreLoggingRetriever storeLoggingRetriever) {
    return ConversationalRetrievalChain.builder()
        .chatLanguageModel(AzureOpenAiChatModel.builder().openAIClient(openAIClient).build())
        .contentRetriever(storeLoggingRetriever)
        .build();
  }
}
