package com.lavr.training.gen.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbeddingModelResponse {
  private String id;
  private Float pointScore;
}
