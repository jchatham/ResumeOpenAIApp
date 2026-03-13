package com.chatham.lc4j.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chatham.lc4j.service.AiService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private AiService aiService;

    @PostMapping
    public String chat(@RequestParam String message) {
        return aiService.ask(message);
    }

}
