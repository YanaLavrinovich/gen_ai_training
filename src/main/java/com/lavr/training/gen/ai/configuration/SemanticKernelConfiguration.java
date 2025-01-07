package com.lavr.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.lavr.training.gen.ai.plugin.AgeCalculatorPlugin;
import com.lavr.training.gen.ai.plugin.LightSwitcherPlugin;
import com.lavr.training.gen.ai.plugin.WeatherForecastPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;

@Configuration
public class SemanticKernelConfiguration {

  @Value("${client-openai-deployment-name}")
  private String defaultDeploymentOrModelName;

  @Bean
  @Scope(value = "prototype")
  public ChatCompletionService chatCompletionService(
      @Value("${client-openai-deployment-name}") String deploymentOrModelName,
      final OpenAIAsyncClient openAIAsyncClient) {
    return OpenAIChatCompletion.builder()
        .withModelId(
            ObjectUtils.isEmpty(deploymentOrModelName)
                ? defaultDeploymentOrModelName
                : deploymentOrModelName)
        .withOpenAIAsyncClient(openAIAsyncClient)
        .build();
  }

  @Bean
  @Scope(value = "prototype")
  public Kernel kernel(final ChatCompletionService chatCompletionService) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .withPlugin(
            KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(), "AgeCalculatorPlugin"))
        .withPlugin(
            KernelPluginFactory.createFromObject(
                new WeatherForecastPlugin(), "WeatherForecastPlugin"))
        .withPlugin(
            KernelPluginFactory.createFromObject(new LightSwitcherPlugin(), "LightSwitcherPlugin"))
        .build();
  }
}
