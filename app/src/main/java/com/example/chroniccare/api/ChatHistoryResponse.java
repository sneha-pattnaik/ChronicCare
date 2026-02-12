package com.example.chroniccare.api;

import java.util.List;

public class ChatHistoryResponse {
    private List<ChatMessage> history;
    private String session_id;

    public List<ChatMessage> getHistory() {
        return history;
    }

    public String getSession_id() {
        return session_id;
    }

    public static class ChatMessage {
        private String role;
        private String content;
        private String timestamp;

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
