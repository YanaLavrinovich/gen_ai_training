package com.lavr.training.gen.ai.controller;

import com.lavr.training.gen.ai.dto.AiModelResponse;
import com.lavr.training.gen.ai.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gen/ai/prompt")
@RequiredArgsConstructor
public class PromptController {

  private final PromptService promptService;

  @GetMapping
  public AiModelResponse getAnswerFromAi(@RequestParam(name = "prompt") String prompt) {
    return promptService.getAnswerFromAi(prompt);
  }
}
