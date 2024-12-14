package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.OpenAiDeploymentList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DialServiceImpl implements DialService {

  private final RestTemplate restTemplate;

  @Value("${client-openai-endpoint}")
  private String openAiEndpoint;

  @Value("${client-openai-key}")
  private String openAiKey;

  @Override
  public OpenAiDeploymentList getDeployments() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Api-Key", openAiKey);
    ResponseEntity<OpenAiDeploymentList> response =
        restTemplate.exchange(
            openAiEndpoint + "/openai/deployments",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            OpenAiDeploymentList.class);
    return response.getBody();
  }
}
