package com.lavr.training.gen.ai.service;

import java.util.List;

public interface PromptService {
  List<String> getAnswerFromAi(String prompt);
}
