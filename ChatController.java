package stanchat.controller;


import stanchat.service.ChatService;
import stanchat.service.MemoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {
    private final ChatService chatService;
    private final MemoryService memoryService;

    public ChatController(ChatService chatService, MemoryService memoryService) {
        this.chatService = chatService;
        this.memoryService = memoryService;
    }

    @PostMapping("/api/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> payload) {
        String userId = payload.getOrDefault("user_id", "test_user");
        String message = payload.getOrDefault("message", "");
        String reply = chatService.chat(userId, message);
        return Map.of("reply", reply);
    }
}