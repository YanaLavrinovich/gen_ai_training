package com.lavr.training.gen.ai.validators;

import com.lavr.training.gen.ai.dto.AiModelRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AiModelParamValidator {

  public List<String> validateAiModelRequest(AiModelRequest request) {
    List<String> errors = new ArrayList<>();
    validateSessionId(request.getSessionId()).ifPresent(errors::add);
    validateTemperature(request.getTemperature()).ifPresent(errors::add);
    return errors;
  }

  public Optional<String> validateSessionId(String sessionId) {
    try {
      UUID.fromString(sessionId);
    } catch (IllegalArgumentException ex) {
      return Optional.of("Session Id should be a valid UUID");
    }
    return Optional.empty();
  }

  private Optional<String> validateTemperature(double temperature) {
    if (temperature < 0 || temperature > 1) {
      return Optional.of("Temperature should be a value between 0 and 1");
    }
    return Optional.empty();
  }
}
