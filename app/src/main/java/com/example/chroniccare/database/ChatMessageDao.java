package com.example.chroniccare.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Insert
    void insert(ChatMessage message);
    
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    List<ChatMessage> getMessagesBySession(String sessionId);
    
    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    void deleteSession(String sessionId);
}
