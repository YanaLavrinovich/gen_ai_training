package com.lavr.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import java.util.ArrayList;
import java.util.List;
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

  public List<String> getAnswerFromAi(String prompt) {
    var results =
        chatCompletionService
            .getChatMessageContentsAsync(prompt, kernel, invocationContext)
            .block();
    List<String> answer = new ArrayList<>();

    if (results == null || results.isEmpty()) {
      log.info("Empty result");
      return answer;
    }

    for (var result : results) {
      log.info(result.getContent());
      answer.add(result.getContent());
    }

    return answer;
  }
}
