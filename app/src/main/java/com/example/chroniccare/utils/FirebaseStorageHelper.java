package com.example.chroniccare.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageHelper {
    private static final String TAG = "FirebaseStorageHelper";
    private final StorageReference storageRef;
    private final String userId;
    
    public FirebaseStorageHelper(String userId) {
        this.userId = userId;
        this.storageRef = FirebaseStorage.getInstance().getReference();
    }
    
    public interface UploadCallback {
        void onSuccess(String downloadUrl);
        void onFailure(String error);
        void onProgress(int progress);
    }
    
    public void uploadDocument(Uri fileUri, String fileName, UploadCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onFailure("User ID is null");
            return;
        }
        
        String path = "users/" + userId + "/documents/" + fileName;
        StorageReference fileRef = storageRef.child(path);
        
        Log.d(TAG, "Uploading file to: " + path);
        
        fileRef.putFile(fileUri)
            .addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                callback.onProgress((int) progress);
            })
            .addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d(TAG, "✅ File uploaded successfully: " + uri.toString());
                    callback.onSuccess(uri.toString());
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Failed to get download URL: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "❌ Upload failed: " + e.getMessage());
                callback.onFailure(e.getMessage());
            });
    }
    
    public void deleteDocument(String fileName) {
        if (userId == null || userId.isEmpty()) return;
        
        String path = "users/" + userId + "/documents/" + fileName;
        storageRef.child(path).delete()
            .addOnSuccessListener(aVoid -> Log.d(TAG, "✅ File deleted from storage"))
            .addOnFailureListener(e -> Log.e(TAG, "❌ Failed to delete file: " + e.getMessage()));
    }
}
