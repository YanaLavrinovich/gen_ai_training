package com.lavr.training.gen.ai.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.azure.ai.openai.models.EmbeddingItem;
import com.lavr.training.gen.ai.dto.EmbeddingModelResponse;

import io.qdrant.client.grpc.Points;

public interface VectorService {

  void saveEmbedding(String text) throws ExecutionException, InterruptedException;

  List<EmbeddingItem> getEmbeddings(String text);

  List<EmbeddingModelResponse> searchClosestEmbeddings(String text) throws ExecutionException, InterruptedException;
}
