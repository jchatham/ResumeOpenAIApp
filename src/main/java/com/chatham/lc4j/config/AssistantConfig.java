package com.chatham.lc4j.config;

import com.chatham.lc4j.service.AiChatModelListener;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import static dev.langchain4j.data.document.loader.ClassPathDocumentLoader.loadDocument;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class AssistantConfig {

    //Demo from logchain4j's account
    @Value("${base-url}")
    private String baseUrl;
    @Value("${api-key}")
    private String apiKey;
    @Value("${model-name}")
    private String modelName;

    //The real stuff - costs money
    @Value("${open.api.key}")
    private String openApiKey;
    @Value("${open.api.model}")
    private String openApiModel;

    private final Integer timeout = 60;
    private final Boolean logRequests = true;
    private final Boolean logResponses = true;

    /**
     * OpenAI API.
     *
     * @return OpenAI chat model.
     */
    @Bean
    public OpenAiChatModel chatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeout))
                .logRequests(logRequests)
                .logResponses(logResponses)
                .build();
    }

    /**
     * Streaming OpenAI API.
     *
     * @return Streaming OpenAI chat model.
     */
    @Bean
    public OpenAiStreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeout))
                .logRequests(logRequests)
                .logResponses(logResponses)
                .build();
    }


    @Bean
    ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        int maxResults = 1;
        double minScore = 0.6;

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(
                OpenAiEmbeddingModel.builder()
                        .apiKey(openApiKey)
                        .modelName(openApiModel)
                        .timeout(Duration.ofSeconds(timeout))
                        .logRequests(logRequests)
                        .logResponses(logResponses));
    }


    /**
     * In-memory embedding store against resume text file in documents
     * @param embeddingModel the embedding model.
     * @param resourceLoader default Spring resource loader.
     * @return embedding store.
     * @throws IOException on error loading file.
     */
    @Bean
    EmbeddingStore<TextSegment> resumeEmbeddingStore(EmbeddingModel embeddingModel,
                                               ResourceLoader resourceLoader) throws IOException {


        //In memory embedding store.
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        Document document = loadDocument("documents/Jeff-Chatham-Resume.txt", new TextDocumentParser());

        //Split document into segments with max token size of 100, convert to embeddings, then store
        //Embeddings are vector representations of data like audio, text, or images.
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0);
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);

        return embeddingStore;
    }

    /**
     * Sets max memory of messages to 10 and also scopes it to prototype.
     *
     * @return chat memory config.
     */
    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ChatMemory chatMemory() {
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
