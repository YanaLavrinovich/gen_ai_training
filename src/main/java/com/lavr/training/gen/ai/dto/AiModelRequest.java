package com.lavr.training.gen.ai.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class AiModelRequest {
  private String sessionId = UUID.randomUUID().toString();
  private String prompt;
  private double temperature = 0.5;
  private String model;
}
