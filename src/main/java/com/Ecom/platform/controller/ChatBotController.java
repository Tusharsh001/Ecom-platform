package com.Ecom.platform.controller;


import com.Ecom.platform.service.ChatBotSeervice;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/api/chat")
public class ChatBotController {

    @Autowired
    private ChatBotSeervice chatBotSeervice;


    @GetMapping("/ask")
    public ResponseEntity<String> askBoot(@RequestParam String message){
        String response=chatBotSeervice.getBotResponse(message);
        return ResponseEntity.ok(response);
    }

}
