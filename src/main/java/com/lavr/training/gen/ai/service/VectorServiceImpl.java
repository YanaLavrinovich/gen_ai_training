package com.lavr.training.gen.ai.service;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.lavr.training.gen.ai.dto.EmbeddingModelResponse;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class VectorServiceImpl implements VectorService {
  private static final String COLLECTION_NAME = "demo_collection";
  private final OpenAIAsyncClient openAIAsyncClient;
  private final QdrantClient qdrantClient;

  @Override
  public void saveEmbedding(String text) throws ExecutionException, InterruptedException {
    var embeddings = getEmbeddings(text);
    var points = new ArrayList<List<Float>>();
    embeddings.forEach(
        embeddingItem -> {
          var values = new ArrayList<>(embeddingItem.getEmbedding());
          points.add(values);
        });

    var pointStructs = new ArrayList<Points.PointStruct>();
    points.forEach(
        point -> {
          var pointStruct = getPointStruct(point, text);
          pointStructs.add(pointStruct);
        });

    saveVector(pointStructs);
  }

  @Override
  public List<EmbeddingItem> getEmbeddings(String text) {
    var embeddings = retrieveEmbeddings(text);
    return embeddings.block().getData();
  }

  @Override
  public List<EmbeddingModelResponse> searchClosestEmbeddings(String text)
      throws ExecutionException, InterruptedException {
    var embeddings = getEmbeddings(text);
    var qe = new ArrayList<Float>();
    embeddings.forEach(embeddingItem -> qe.addAll(embeddingItem.getEmbedding()));
    var result =
        qdrantClient
            .searchAsync(
                Points.SearchPoints.newBuilder()
                    .setCollectionName(COLLECTION_NAME)
                    .addAllVector(qe)
                    .setLimit(5)
                    .build())
            .get();
    return result.stream()
        .map(
            emb ->
                EmbeddingModelResponse.builder()
                    .id(emb.getId().getUuid())
                    .pointScore(emb.getScore())
                    .build())
        .toList();
  }

  private void saveVector(ArrayList<Points.PointStruct> pointStructs)
      throws InterruptedException, ExecutionException {
    createCollection();
    var updateResult = qdrantClient.upsertAsync(COLLECTION_NAME, pointStructs).get();
    log.info(updateResult.getStatus().name());
  }

  private Points.PointStruct getPointStruct(List<Float> point, String text) {
    return Points.PointStruct.newBuilder()
        .setId(id(UUID.randomUUID()))
        .setVectors(vectors(point))
        .putAllPayload(Map.of("text", value(text)))
        .build();
  }

  private void createCollection() throws InterruptedException, ExecutionException {
    try {
      qdrantClient.getCollectionInfoAsync(COLLECTION_NAME).get();
    } catch (Exception ex) {
      log.info("Collection '{}' not found", COLLECTION_NAME);
      var result =
          qdrantClient
              .createCollectionAsync(
                  COLLECTION_NAME,
                  Collections.VectorParams.newBuilder()
                      .setDistance(Collections.Distance.Dot)
                      .setSize(1536)
                      .build())
              .get();
      log.info("Collection was created: {}", result.getResult());
    }
  }

  private Mono<Embeddings> retrieveEmbeddings(String text) {
    var qembeddingsOptions = new EmbeddingsOptions(List.of(text));
    return openAIAsyncClient.getEmbeddings("text-embedding-ada-002", qembeddingsOptions);
  }
}
