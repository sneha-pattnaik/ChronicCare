package com.example.chroniccare.api;

public class ChatRequest {
    private String session_id;
    private String message;

    public ChatRequest(String session_id, String message) {
        this.session_id = session_id;
        this.message = message;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getMessage() {
        return message;
    }
}
