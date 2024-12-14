package com.lavr.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.lavr.training.gen.ai.dto.AiModelRequest;
import com.lavr.training.gen.ai.dto.AiModelResponse;
import com.lavr.training.gen.ai.history.ChatHistoryStorage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class PromptServiceImpl implements PromptService {

  private final OpenAIAsyncClient openAIAsyncClient;
  private final ChatHistoryStorage chatHistoryStorage;

  public AiModelResponse getAnswerFromAi(AiModelRequest request) {
    ChatHistory chatHistory = chatHistoryStorage.get(request.getSessionId());
    chatHistory.addUserMessage(request.getPrompt());

    ChatCompletionService chatCompletionService =
        getChatCompletionService(request.getModel(), openAIAsyncClient);
    var results =
        chatCompletionService
            .getChatMessageContentsAsync(
                chatHistory,
                getKernel(chatCompletionService),
                buildInvocationContext(request.getTemperature()))
            .block();

    if (results == null || results.isEmpty()) {
      log.info("Empty result");
      return AiModelResponse.builder().build();
    }

    String answer =
        results.stream().map(ChatMessageContent::getContent).collect(Collectors.joining());
    log.info(answer);
    chatHistory.addAssistantMessage(answer);

    return AiModelResponse.builder().response(answer).sessionId(request.getSessionId()).build();
  }

  @Override
  public void clearHistory(String sessionId) {
    chatHistoryStorage.remove(sessionId);
  }

  @Lookup("chatCompletionService")
  protected ChatCompletionService getChatCompletionService(
      String model, OpenAIAsyncClient openAIAsyncClient) {
    return null;
  }

  @Lookup("kernel")
  protected Kernel getKernel(ChatCompletionService chatCompletionService) {
    return null;
  }

  private InvocationContext buildInvocationContext(double temperature) {
    return new InvocationContext.Builder()
        .withPromptExecutionSettings(
            PromptExecutionSettings.builder().withTemperature(temperature).build())
        .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
        .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
        .build();
  }
}
