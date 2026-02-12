package com.example.chroniccare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chroniccare.database.AppDatabase;
import com.example.chroniccare.database.MedicalDocument;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DocumentsActivity extends BaseActivity {

    private MaterialToolbar toolbar;
    private LinearLayout documentsContainer;
    private TextView tvEmptyState;
    private AppDatabase db;
    private String currentUserId;
    private com.example.chroniccare.utils.FirebaseSync firebaseSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        SharedPreferences prefs = getSharedPreferences("ChronicCarePrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("userId", "");
        db = AppDatabase.getInstance(this);
        firebaseSync = new com.example.chroniccare.utils.FirebaseSync(currentUserId);

        toolbar = findViewById(R.id.toolbar);
        documentsContainer = findViewById(R.id.documentsContainer);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        toolbar.setNavigationOnClickListener(v -> finish());
        syncDocumentsFromCloud();
        loadDocuments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDocuments();
    }
    
    private void syncDocumentsFromCloud() {
        // Fetch documents from Firestore
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
            .collection("users").document(currentUserId)
            .collection("documents")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                if (!querySnapshot.isEmpty()) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            try {
                                // Check if document already exists in local DB
                                String downloadUrl = doc.getString("downloadUrl");
                                if (downloadUrl != null) {
                                    List<MedicalDocument> existing = db.medicalDocumentDao().getDocumentsByUserId(currentUserId);
                                    boolean exists = false;
                                    for (MedicalDocument existingDoc : existing) {
                                        if (downloadUrl.equals(existingDoc.documentUri)) {
                                            exists = true;
                                            break;
                                        }
                                    }
                                    
                                    if (!exists) {
                                        // Add to local database
                                        MedicalDocument newDoc = new MedicalDocument();
                                        newDoc.userId = currentUserId;
                                        newDoc.documentName = doc.getString("documentName");
                                        newDoc.documentType = doc.getString("documentType");
                                        newDoc.documentUri = downloadUrl;
                                        newDoc.uploadDate = doc.getLong("uploadDate") != null ? doc.getLong("uploadDate") : System.currentTimeMillis();
                                        newDoc.fileSize = doc.getLong("fileSize") != null ? doc.getLong("fileSize") : 0;
                                        
                                        db.medicalDocumentDao().insert(newDoc);
                                        Log.d("DocumentsActivity", "✅ Synced document from cloud: " + newDoc.documentName);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("DocumentsActivity", "Error syncing document: " + e.getMessage());
                            }
                        }
                        runOnUiThread(this::loadDocuments);
                    });
                }
            })
            .addOnFailureListener(e -> Log.e("DocumentsActivity", "Failed to sync from cloud: " + e.getMessage()));
    }

    private void loadDocuments() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<MedicalDocument> documents = db.medicalDocumentDao().getDocumentsByUserId(currentUserId);
            runOnUiThread(() -> {
                documentsContainer.removeAllViews();
                if (documents.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    documentsContainer.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    documentsContainer.setVisibility(View.VISIBLE);
                    for (MedicalDocument doc : documents) {
                        addDocumentCard(doc);
                    }
                }
            });
        });
    }

    private void addDocumentCard(MedicalDocument doc) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_document, documentsContainer, false);
        
        ImageView ivIcon = cardView.findViewById(R.id.ivDocIcon);
        ImageView ivPreview = cardView.findViewById(R.id.ivDocPreview);
        TextView tvName = cardView.findViewById(R.id.tvDocName);
        TextView tvType = cardView.findViewById(R.id.tvDocType);
        TextView tvDate = cardView.findViewById(R.id.tvDocDate);
        TextView tvSize = cardView.findViewById(R.id.tvDocSize);
        ImageView btnDelete = cardView.findViewById(R.id.btnDeleteDoc);

        tvName.setText(doc.documentName);
        tvType.setText(doc.documentType);
        tvDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(doc.uploadDate)));
        tvSize.setText(formatFileSize(doc.fileSize));

        // Load preview for images from Firebase Storage URL
        String documentUri = doc.documentUri;
        if (documentUri != null && documentUri.startsWith("https://")) {
            // Firebase Storage URL - load with Picasso
            if (documentUri.contains(".jpg") || documentUri.contains(".jpeg") || documentUri.contains(".png")) {
                ivPreview.setVisibility(android.view.View.VISIBLE);
                ivIcon.setVisibility(android.view.View.GONE);
                com.squareup.picasso.Picasso.get().load(documentUri).into(ivPreview);
            } else {
                ivPreview.setVisibility(android.view.View.GONE);
                ivIcon.setVisibility(android.view.View.VISIBLE);
            }
        } else {
            // Local URI (legacy)
            Uri uri = Uri.parse(documentUri);
            String mimeType = getContentResolver().getType(uri);
            if (mimeType != null && mimeType.startsWith("image/")) {
                ivPreview.setVisibility(android.view.View.VISIBLE);
                ivIcon.setVisibility(android.view.View.GONE);
                ivPreview.setImageURI(uri);
            } else {
                ivPreview.setVisibility(android.view.View.GONE);
                ivIcon.setVisibility(android.view.View.VISIBLE);
            }
        }

        cardView.setOnClickListener(v -> openDocument(doc.documentUri));
        btnDelete.setOnClickListener(v -> confirmDelete(doc));

        documentsContainer.addView(cardView);
    }

    private void openDocument(String uriString) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(uriString);
            intent.setDataAndType(uri, "application/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Open with"));
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open document", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete(MedicalDocument doc) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Document")
            .setMessage("Are you sure you want to delete " + doc.documentName + "?")
            .setPositiveButton("Delete", (dialog, which) -> deleteDocument(doc))
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteDocument(MedicalDocument doc) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.medicalDocumentDao().delete(doc);
            runOnUiThread(() -> {
                Toast.makeText(this, "Document deleted", Toast.LENGTH_SHORT).show();
                loadDocuments();
            });
        });
        
        // Delete from Firebase
        firebaseSync.deleteDocument(String.valueOf(doc.id));
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format(Locale.getDefault(), "%.1f KB", size / 1024.0);
        return String.format(Locale.getDefault(), "%.1f MB", size / (1024.0 * 1024.0));
    }
}
