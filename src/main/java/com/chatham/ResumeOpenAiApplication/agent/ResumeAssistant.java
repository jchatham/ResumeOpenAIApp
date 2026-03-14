package com.chatham.ResumeOpenAiApplication.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ResumeAssistant {
    @SystemMessage("""
            You are a helpful AI agent providing information about Jeff Chatham's resume.
            Today is {{current_date}}.
            """)
    String answer(String userMessage);
}
