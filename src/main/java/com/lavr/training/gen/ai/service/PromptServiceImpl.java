package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.AiModelResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptServiceImpl implements PromptService {

  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final InvocationContext invocationContext;

  public AiModelResponse getAnswerFromAi(String prompt) {
    var results =
        chatCompletionService
            .getChatMessageContentsAsync(prompt, kernel, invocationContext)
            .block();

    if (results == null || results.isEmpty()) {
      log.info("Empty result");
      return AiModelResponse.builder().build();
    }

    StringBuilder answer = new StringBuilder();
    results.forEach(
        result -> {
          log.info(result.getContent());
          answer.append(result.getContent());
        });

    return AiModelResponse.builder().response(answer.toString()).build();
  }
}
