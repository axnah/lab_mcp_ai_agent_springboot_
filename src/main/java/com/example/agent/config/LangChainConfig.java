package com.example.agent.config;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.service.AiServices;
import com.example.agent.agent.BacklogAgent;
import com.example.agent.tools.AgentTool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

@Configuration
public class LangChainConfig {
    @Bean
    public AnthropicChatModel anthropicChatModel(
            @Value("${anthropic.api-key}") String apiKey,
            @Value("${anthropic.model}") String model,
            @Value("${anthropic.timeout-seconds:60}") Integer timeoutSeconds) {
        return AnthropicChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    @Bean
    public BacklogAgent backlogAgent(AnthropicChatModel model,
            ObjectProvider<List<AgentTool>> toolBeansProvider) {
        List<AgentTool> toolBeans = toolBeansProvider.getIfAvailable(List::of);

        var builder = AiServices.builder(BacklogAgent.class)
                .chatModel(model);
        if (toolBeans != null && !toolBeans.isEmpty()) {
            builder.tools(new ArrayList<Object>(toolBeans));
        }
        return builder.build();
    }
}
