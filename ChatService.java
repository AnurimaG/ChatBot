package stanchat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {
    private final MemoryService memoryService;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String openaiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${bot.name:Aria}")
    private String botName;

    public ChatService(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    public String chat(String userId, String userMessage) {
        // ✅ Store facts if user shares them
        storePossibleFacts(userId, userMessage);

        try {
            return callOpenAI(userId, userMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I’m having trouble thinking right now.";
        }
    }

    private String callOpenAI(String userId, String userMessage) throws Exception {
        if (openaiKey == null || openaiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY not set.");
        }

        var root = mapper.createObjectNode();
        root.put("model", model);

        var messages = mapper.createArrayNode();
        // System prompt
        messages.add(mapper.createObjectNode().put("role", "system").put("content",
                "You are " + botName + ", a helpful, empathetic chatbot. " +
                        "You remember personal facts the user has told you earlier. " +
                        "Respond naturally, in a friendly and human-like way."));

        // ✅ Inject memory into context
        List<String> memory = memoryService.getMemory(userId);
        if (!memory.isEmpty()) {
            messages.add(mapper.createObjectNode().put("role", "system")
                    .put("content", "Here are facts you know about the user: " + memory));
        }

        // User input
        messages.add(mapper.createObjectNode().put("role", "user").put("content", userMessage));
        root.set("messages", messages);

        String payload = mapper.writeValueAsString(root);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = mapper.readTree(response.body());

        return json.get("choices").get(0).get("message").get("content").asText();
    }

    // ✅ Smarter fact extraction
    private void storePossibleFacts(String userId, String text) {
        String lower = text.toLowerCase();

        // Favorite color
        Pattern colorPat = Pattern.compile("my favorite color is ([a-zA-Z\\s]+)");
        Matcher m = colorPat.matcher(lower);
        if (m.find()) {
            memoryService.addMemory(userId, "favorite color is " + m.group(1).trim());
            return;
        }

        // Name
        Pattern namePat = Pattern.compile("my name is ([a-zA-Z\\s]+)");
        Matcher m2 = namePat.matcher(lower);
        if (m2.find()) {
            memoryService.addMemory(userId, "name is " + m2.group(1).trim());
            return;
        }

        // City
        Pattern livePat = Pattern.compile("i (?:live|stay) in ([a-zA-Z\\s]+)");
        Matcher m3 = livePat.matcher(lower);
        if (m3.find()) {
            memoryService.addMemory(userId, "lives in " + m3.group(1).trim());
            return;
        }

        // Hobby
        Pattern hobbyPat = Pattern.compile("my hobby is ([a-zA-Z\\s]+)");
        Matcher m4 = hobbyPat.matcher(lower);
        if (m4.find()) {
            memoryService.addMemory(userId, "hobby is " + m4.group(1).trim());
            return;
        }

        // Pet
        Pattern petPat = Pattern.compile("i have a (dog|cat|parrot|fish|pet)");
        Matcher m5 = petPat.matcher(lower);
        if (m5.find()) {
            memoryService.addMemory(userId, "has a pet: " + m5.group(1).trim());
            return;
        }

        // Birthday
        Pattern bdayPat = Pattern.compile("my birthday is on ([a-zA-Z0-9\\s]+)");
        Matcher m6 = bdayPat.matcher(lower);
        if (m6.find()) {
            memoryService.addMemory(userId, "birthday is on " + m6.group(1).trim());
        }
    }
}