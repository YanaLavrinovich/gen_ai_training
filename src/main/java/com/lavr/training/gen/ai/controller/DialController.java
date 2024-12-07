package com.lavr.training.gen.ai.controller;

import com.lavr.training.gen.ai.dto.OpenAiDeploymentList;
import com.lavr.training.gen.ai.service.DialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("openai")
@RequiredArgsConstructor
public class DialController {

  private final DialService dialService;

  @GetMapping("/deployments")
  public ResponseEntity<OpenAiDeploymentList> getDeployments() {
    return ResponseEntity.ok(dialService.getDeployments());
  }
}
