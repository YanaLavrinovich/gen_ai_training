package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.AiModelResponse;

public interface PromptService {

  /**
   * Get response from AI model based on the prompt
   *
   * @param prompt prompt from user
   * @return {@link AiModelResponse} with model response
   */
  AiModelResponse getAnswerFromAi(String sessionId, String prompt, double temperature);

  /**
   * Clear chat history for specified session id
   *
   * @param sessionId session Id
   */
  void clearHistory(String sessionId);
}
