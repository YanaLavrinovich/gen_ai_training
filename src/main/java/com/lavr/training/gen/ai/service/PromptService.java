package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.AiModelRequest;
import com.lavr.training.gen.ai.dto.AiModelResponse;

public interface PromptService {

  /**
   * Get response from AI model based on the prompt
   *
   * @param request {@link AiModelRequest} request with prompt from user
   * @return {@link AiModelResponse} with model response
   */
  AiModelResponse getAnswerFromAi(AiModelRequest request);

  /**
   * Clear chat history for specified session id
   *
   * @param sessionId session Id
   */
  void clearHistory(String sessionId);
}
