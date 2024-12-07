package com.lavr.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfiguration {

  @Bean
  public OpenAIAsyncClient openAIAsyncClient(
      @Value("${client-openai-key}") String clientKey,
      @Value("${client-openai-endpoint}") String clientEndpoint) {
    return new OpenAIClientBuilder()
        .credential(new AzureKeyCredential(clientKey))
        .endpoint(clientEndpoint)
        .buildAsyncClient();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
