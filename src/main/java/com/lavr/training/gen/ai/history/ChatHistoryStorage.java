package com.lavr.training.gen.ai.history;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChatHistoryStorage {
  private static final Map<String, ChatHistory> STORAGE = new HashMap<>();

  public ChatHistory get(String sessionId) {
    if (!STORAGE.containsKey(sessionId)) {
      STORAGE.computeIfAbsent(sessionId, k -> new ChatHistory());
    }
    return STORAGE.get(sessionId);
  }

  public void remove(String sessionId) {
    STORAGE.remove(sessionId);
  }
}
