package com.example.chroniccare.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_messages")
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String sessionId;
    public String role; // "user" or "assistant"
    public String content;
    public long timestamp;
}
