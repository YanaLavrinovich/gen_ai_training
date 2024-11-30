package com.lavr.training.gen.ai.controller;

import com.lavr.training.gen.ai.dto.AiModelRequest;
import com.lavr.training.gen.ai.service.PromptService;
import com.lavr.training.gen.ai.validators.AiModelParamValidator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gen/ai")
@RequiredArgsConstructor
public class PromptController {

  private final PromptService promptService;
  private final AiModelParamValidator paramValidator;

  @PostMapping
  public ResponseEntity<?> getAnswerFromAi(@RequestBody AiModelRequest request) {
    List<String> errors = paramValidator.validateAiModelRequest(request);
    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(errors);
    }

    return ResponseEntity.ok(
        promptService.getAnswerFromAi(
            request.getSessionId(), request.getPrompt(), request.getTemperature()));
  }

  @DeleteMapping("/history")
  public ResponseEntity<?> clearHistory(@RequestParam(name = "sessionId") String sessionId) {
    Optional<String> error = paramValidator.validateSessionId(sessionId);
    if (error.isPresent()) {
      return ResponseEntity.badRequest().body(error.get());
    }

    promptService.clearHistory(sessionId);
    return ResponseEntity.noContent().build();
  }
}
