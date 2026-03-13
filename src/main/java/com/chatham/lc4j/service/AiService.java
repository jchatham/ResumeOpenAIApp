package com.chatham.lc4j.service;

import dev.langchain4j.model.openai.OpenAiChatModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Autowired
    private OpenAiChatModel model;

    public String ask(String question) {

        String prompt = """
        You are an assistant answering questions about a document.

        Question:
        %s
        """.formatted(question);

        return model.chat(prompt);
    }

}
