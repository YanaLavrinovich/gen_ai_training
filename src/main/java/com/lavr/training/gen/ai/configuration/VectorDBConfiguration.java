package com.lavr.training.gen.ai.configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorDBConfiguration {

  @Bean
  public QdrantClient qdrantClient(
      @Value("${vector.db.qdrant.host}") String host,
      @Value("${vector.db.qdrant.port}") Integer port) {
    return new QdrantClient(QdrantGrpcClient.newBuilder(host, port, false).build());
  }
}
