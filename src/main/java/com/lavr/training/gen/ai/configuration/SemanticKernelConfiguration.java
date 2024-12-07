package com.lavr.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.lavr.training.gen.ai.plugin.SimplePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SemanticKernelConfiguration {

  @Bean
  public ChatCompletionService chatCompletionService(
      @Value("${client-openai-deployment-name}") String deploymentOrModelName,
      final OpenAIAsyncClient openAIAsyncClient) {
    return OpenAIChatCompletion.builder()
        .withModelId(deploymentOrModelName)
        .withOpenAIAsyncClient(openAIAsyncClient)
        .build();
  }

  @Bean
  public KernelPlugin kernelPlugin() {
    return KernelPluginFactory.createFromObject(new SimplePlugin(), "SimplePlugin");
  }

  @Bean
  public Kernel kernel(
      final ChatCompletionService chatCompletionService, final KernelPlugin kernelPlugin) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .withPlugin(kernelPlugin)
        .build();
  }
}
