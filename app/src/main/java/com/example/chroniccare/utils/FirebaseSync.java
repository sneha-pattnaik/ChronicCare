package com.example.chroniccare.utils;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirebaseSync {
    private static final String TAG = "FirebaseSync";
    private final FirebaseFirestore db;
    private final String userId;
    
    public FirebaseSync(String userId) {
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        Log.d(TAG, "FirebaseSync initialized with userId: " + (userId != null ? userId : "NULL"));
    }
    
    // Profile - Personal Info
    public void syncPersonalInfo(Map<String, Object> personalData) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "Cannot sync personal info: userId is null or empty");
            return;
        }
        
        Log.d(TAG, "Syncing personal info for user: " + userId);
        
        db.collection("users").document(userId)
            .collection("profile").document("personalInfo")
            .set(personalData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Personal info synced"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Personal info sync failed: " + e.getMessage(), e));
    }
    
    // Profile - Medical Info
    public void syncMedicalInfo(Map<String, Object> medicalData) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "Cannot sync medical info: userId is null or empty");
            return;
        }
        
        Log.d(TAG, "Syncing medical info for user: " + userId);
        
        db.collection("users").document(userId)
            .collection("profile").document("medicalInfo")
            .set(medicalData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Medical info synced"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Medical info sync failed: " + e.getMessage(), e));
    }
    
    // Profile - Emergency Contact
    public void syncEmergencyContact(Map<String, Object> emergencyData) {
        if (userId == null || userId.isEmpty()) return;
        
        db.collection("users").document(userId)
            .collection("profile").document("emergencyContact")
            .set(emergencyData)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Emergency contact synced"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Emergency contact sync failed: " + e.getMessage()));
    }
    
    // Documents
    public void syncDocument(String docId, Map<String, Object> data) {
        if (userId == null || userId.isEmpty()) return;
        
        Log.d(TAG, "Syncing document: " + docId);
        
        db.collection("users").document(userId)
            .collection("documents").document(docId)
            .set(data)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ Document synced"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Document sync failed: " + e.getMessage()));
    }
    
    public void deleteDocument(String docId) {
        if (userId == null || userId.isEmpty()) return;
        db.collection("users").document(userId).collection("documents").document(docId).delete();
    }
    
    // Medications (already exists, keeping for consistency)
    public void syncMedication(String medicationId, Map<String, Object> data) {
        if (userId == null || userId.isEmpty()) return;
        db.collection("users").document(userId).collection("medications").document(medicationId).set(data);
    }
    
    public void deleteMedication(String medicationId) {
        if (userId == null || userId.isEmpty()) return;
        db.collection("users").document(userId).collection("medications").document(medicationId).delete();
    }
    
    // Dr.GPT Chats
    public void syncChatMessage(String sessionId, Map<String, Object> messageData) {
        if (userId == null || userId.isEmpty()) return;
        
        db.collection("users").document(userId)
            .collection("drGptChats").document(sessionId)
            .collection("messages").add(messageData)
            .addOnSuccessListener(doc -> Log.d(TAG, "✅ Chat message synced"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Chat message sync failed: " + e.getMessage()));
    }
    
    public void clearChatSession(String sessionId) {
        if (userId == null || userId.isEmpty()) return;
        
        db.collection("users").document(userId)
            .collection("drGptChats").document(sessionId)
            .collection("messages").get()
            .addOnSuccessListener(querySnapshot -> {
                for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    doc.getReference().delete();
                }
                Log.d(TAG, "✅ Chat session cleared");
            });
    }
    
    // Exercise Logs
    public void syncExercise(String exerciseId, Map<String, Object> data) {
        if (userId == null || userId.isEmpty()) return;
        db.collection("users").document(userId).collection("exercises").document(exerciseId).set(data);
    }
    
    // Food Logs
    public void syncFood(String foodId, Map<String, Object> data) {
        if (userId == null || userId.isEmpty()) return;
        db.collection("users").document(userId).collection("foods").document(foodId).set(data);
    }
}
