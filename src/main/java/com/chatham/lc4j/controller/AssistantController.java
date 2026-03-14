package com.chatham.lc4j.controller;


import com.chatham.lc4j.agent.Assistant;
import com.chatham.lc4j.agent.ResumeAssistant;
import com.chatham.lc4j.agent.StreamingAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;


@RestController
@RequestMapping("/chat")
public class AssistantController {
    @Autowired
    private Assistant assistant;

    @Autowired
    private StreamingAssistant streamingAssistant;

    @Autowired
    private ResumeAssistant resumeAssistant;

    @GetMapping("/assistant")
    public String chat(String message) {
        return assistant.answer(message);
    }

    @GetMapping(value = "/streamingAssistant", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamingAssistant(
            @RequestParam(value = "message", defaultValue = "What is the current time?") String message) {
        return streamingAssistant.answer(message);
    }

    @GetMapping("/resume")
    public String resume(String message) {
        return resumeAssistant.answer(message);
    }
}
