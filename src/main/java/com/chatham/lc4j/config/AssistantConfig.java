package com.chatham.lc4j.config;

import com.chatham.lc4j.service.AiChatModelListener;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Scope;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
@Configuration
public class AssistantConfig {

    @Value("${base-url}")
    private String baseUrl;
    @Value("${api-key}")
    private String apiKey;
    @Value("${model-name}")
    private String modelName;

    /**
     * Using demo endpoint from langchain4j.
     * @return The model.
     */
    @Bean
    public OpenAiChatModel chatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }

    @Bean
    public OpenAiStreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }

    /**
     * Sets max memory of messages to 10 and also scopes it to prototype.
     * @return chat memory config.
     */
    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.withMaxMessages(10);
    }

    /**
     * @return AiChatModelListener imple.
     */
    @Bean
    ChatModelListener chatModelListener() {
        return new AiChatModelListener();
    }
}
