package com.chatham.ResumeOpenAiApplication.controller;


import com.chatham.ResumeOpenAiApplication.agent.ResumeAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chat")
public class AssistantController {
    @Autowired
    private ResumeAssistant resumeAssistant;

    @GetMapping("/resume")
    public String resume(String message) {
        return resumeAssistant.answer(message);
    }
}
