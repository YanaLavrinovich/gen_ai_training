package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.AiModelResponse;
import com.lavr.training.gen.ai.history.ChatHistoryStorage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptServiceImpl implements PromptService {

  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final ChatHistoryStorage chatHistoryStorage;

  public AiModelResponse getAnswerFromAi(String sessionId, String prompt, double temperature) {
    ChatHistory chatHistory = chatHistoryStorage.get(sessionId);
    chatHistory.addUserMessage(prompt);

    StringBuilder answer = new StringBuilder();
    var results =
        chatCompletionService
            .getChatMessageContentsAsync(chatHistory, kernel, buildInvocationContext(temperature))
            .block();

    if (results == null || results.isEmpty()) {
      log.info("Empty result");
      return AiModelResponse.builder().build();
    }

    results.stream()
        .filter(result -> result.getAuthorRole() == AuthorRole.ASSISTANT)
        .forEach(
            result -> {
              log.info(result.getContent());
              chatHistory.addAssistantMessage(result.getContent());
              answer.append(result.getContent());
            });

    return AiModelResponse.builder().response(answer.toString()).build();
  }

  @Override
  public void clearHistory(String sessionId) {
    chatHistoryStorage.remove(sessionId);
  }

  private InvocationContext buildInvocationContext(double temperature) {
    return new InvocationContext.Builder()
        .withPromptExecutionSettings(
            PromptExecutionSettings.builder()
                .withTemperature(temperature)
                .withMaxTokens(100)
                .build())
        .build();
  }
}
