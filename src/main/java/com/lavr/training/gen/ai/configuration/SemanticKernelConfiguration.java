package com.lavr.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.lavr.training.gen.ai.plugin.SimplePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SemanticKernelConfiguration {

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

  @Bean
  public InvocationContext invocationContext() {
    return new InvocationContext.Builder()
        .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
        .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
        .build();
  }
}
