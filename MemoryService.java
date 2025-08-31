package chat.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MemoryService {
    private final Map<String, List<String>> memoryMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File memoryFile = new File("memory.json");

    public MemoryService() {
        loadMemoryFromFile();
    }

    public void addMemory(String userId, String fact) {
        memoryMap.computeIfAbsent(userId, k -> new ArrayList<>())
                .add(fact + " (at " + LocalDateTime.now() + ")");
        saveMemoryToFile();
    }

    public List<String> getMemory(String userId) {
        return memoryMap.getOrDefault(userId, Collections.emptyList());
    }

    private void saveMemoryToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(memoryFile, memoryMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMemoryFromFile() {
        if (memoryFile.exists()) {
            try {
                Map<String, List<String>> loaded =
                        mapper.readValue(memoryFile, new TypeReference<>() {});
                memoryMap.putAll(loaded);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

