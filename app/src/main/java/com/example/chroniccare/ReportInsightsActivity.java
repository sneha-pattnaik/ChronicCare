package com.example.chroniccare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chroniccare.database.AppDatabase;
import com.example.chroniccare.database.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportInsightsActivity extends AppCompatActivity {

    private TextView tvReportTitle;
    private TextView tvAnalysisStatus;
    private TextView tvSummaryPlain;
    private TextView tvSummaryDetailed;
    private TextView tvConfidence;
    private TextView tvReviewStatus;
    private LinearLayout containerFindings;
    private LinearLayout containerKnown;
    private LinearLayout containerUnclear;
    private LinearLayout containerSources;
    private Button btnAnalyzeReport;
    private Button btnOpenReport;
    private Button btnRequestReview;
    private Button btnMarkVerified;

    private FirebaseFirestore firestore;
    private AppDatabase db;
    private String userId;
    private String documentId;
    private String documentName;
    private String documentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_insights);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();

        firestore = FirebaseFirestore.getInstance();
        db = AppDatabase.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("ChronicCarePrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        boolean isDoctor = prefs.getBoolean("isDoctor", false);
        if (isDoctor) {
            btnMarkVerified.setVisibility(View.VISIBLE);
        }

        documentId = getIntent().getStringExtra("documentId");
        documentName = getIntent().getStringExtra("documentName");
        documentUrl = getIntent().getStringExtra("documentUrl");

        tvReportTitle.setText(documentName != null ? documentName : "Report");

        btnOpenReport.setOnClickListener(v -> openReport());
        btnAnalyzeReport.setOnClickListener(v -> requestAnalysis());
        btnRequestReview.setOnClickListener(v -> requestDoctorReview());
        btnMarkVerified.setOnClickListener(v -> markAsVerified());

        if (TextUtils.isEmpty(documentId) || TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "Report details missing", Toast.LENGTH_SHORT).show();
            return;
        }

        subscribeToReportUpdates();
    }

    private void initViews() {
        tvReportTitle = findViewById(R.id.tvReportTitle);
        tvAnalysisStatus = findViewById(R.id.tvAnalysisStatus);
        tvSummaryPlain = findViewById(R.id.tvSummaryPlain);
        tvSummaryDetailed = findViewById(R.id.tvSummaryDetailed);
        tvConfidence = findViewById(R.id.tvConfidence);
        tvReviewStatus = findViewById(R.id.tvReviewStatus);
        containerFindings = findViewById(R.id.containerFindings);
        containerKnown = findViewById(R.id.containerKnown);
        containerUnclear = findViewById(R.id.containerUnclear);
        containerSources = findViewById(R.id.containerSources);
        btnAnalyzeReport = findViewById(R.id.btnAnalyzeReport);
        btnOpenReport = findViewById(R.id.btnOpenReport);
        btnRequestReview = findViewById(R.id.btnRequestReview);
        btnMarkVerified = findViewById(R.id.btnMarkVerified);
    }

    private void subscribeToReportUpdates() {
        firestore.collection("users").document(userId)
            .collection("documents").document(documentId)
            .addSnapshotListener((snapshot, error) -> {
                if (error != null || snapshot == null || !snapshot.exists()) {
                    return;
                }

                String status = snapshot.getString("analysisStatus");
                tvAnalysisStatus.setText("Status: " + (status != null ? status : "Not analyzed"));

                Map<String, Object> analysis = safeMap(snapshot.get("analysis"));
                updateAnalysisUI(analysis);

                Map<String, Object> review = safeMap(snapshot.get("review"));
                updateReviewUI(review);
            });
    }

    private void updateAnalysisUI(Map<String, Object> analysis) {
        if (analysis.isEmpty()) {
            tvSummaryPlain.setText("No summary yet.");
            tvSummaryDetailed.setText("");
            tvConfidence.setText("");
            renderList(containerFindings, null, "No key findings yet.");
            renderList(containerKnown, null, "");
            renderList(containerUnclear, null, "");
            renderSources(containerSources, null);
            return;
        }

        tvSummaryPlain.setText(getStringValue(analysis.get("summaryPlain"), "No summary yet."));
        tvSummaryDetailed.setText(getStringValue(analysis.get("summaryDetailed"), ""));

        Double confidence = getDoubleValue(analysis.get("confidence"));
        if (confidence != null) {
            tvConfidence.setText(String.format("Confidence: %.0f%%", confidence * 100));
        } else {
            tvConfidence.setText("");
        }

        renderList(containerFindings, toStringList(analysis.get("keyFindings")), "No key findings yet.");
        renderList(containerKnown, toStringList(analysis.get("known")), "");
        renderList(containerUnclear, toStringList(analysis.get("uncertain")), "");
        renderSources(containerSources, toMapList(analysis.get("sources")));
    }

    private void updateReviewUI(Map<String, Object> review) {
        if (review.isEmpty()) {
            tvReviewStatus.setText("Status: Not requested");
            return;
        }
        String status = getStringValue(review.get("status"), "Not requested");
        String reviewer = getStringValue(review.get("reviewerName"), "");
        if (!reviewer.isEmpty()) {
            tvReviewStatus.setText("Status: " + status + " (by " + reviewer + ")");
        } else {
            tvReviewStatus.setText("Status: " + status);
        }
    }

    private void requestAnalysis() {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(documentId)) {
            Toast.makeText(this, "Unable to request analysis", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference personalRef = firestore.collection("users").document(userId)
            .collection("profile").document("personalInfo");
        DocumentReference medicalRef = firestore.collection("users").document(userId)
            .collection("profile").document("medicalInfo");
        DocumentReference emergencyRef = firestore.collection("users").document(userId)
            .collection("profile").document("emergencyContact");
        DocumentReference docRef = firestore.collection("users").document(userId)
            .collection("documents").document(documentId);

        Tasks.whenAllSuccess(personalRef.get(), medicalRef.get(), emergencyRef.get(),
                firestore.collection("users").document(userId).collection("medications").get())
            .addOnSuccessListener(results -> {
                DocumentSnapshot personalDoc = (DocumentSnapshot) results.get(0);
                DocumentSnapshot medicalDoc = (DocumentSnapshot) results.get(1);
                DocumentSnapshot emergencyDoc = (DocumentSnapshot) results.get(2);
                QuerySnapshot medsSnapshot = (QuerySnapshot) results.get(3);

                User localUser = db.userDao().getUserByUserId(userId);
                Map<String, Object> profile = buildProfileMap(personalDoc, medicalDoc, emergencyDoc, localUser);
                List<Map<String, Object>> meds = buildMedicationList(medsSnapshot);

                Map<String, Object> request = new HashMap<>();
                request.put("documentId", documentId);
                request.put("documentName", documentName);
                request.put("documentUrl", documentUrl);
                request.put("requestedAt", System.currentTimeMillis());
                request.put("status", "queued");
                request.put("profile", profile);
                request.put("medications", meds);

                docRef.collection("analysisRequests").add(request);
                docRef.update("analysisStatus", "queued", "analysisRequestedAt", System.currentTimeMillis());

                Toast.makeText(this, "Analysis requested", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                docRef.update("analysisStatus", "queued", "analysisRequestedAt", System.currentTimeMillis());
                Toast.makeText(this, "Analysis requested", Toast.LENGTH_SHORT).show();
            });
    }

    private void requestDoctorReview() {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(documentId)) {
            return;
        }
        Map<String, Object> review = new HashMap<>();
        review.put("status", "requested");
        review.put("requestedAt", System.currentTimeMillis());
        firestore.collection("users").document(userId)
            .collection("documents").document(documentId)
            .update("review", review);
        Toast.makeText(this, "Doctor review requested", Toast.LENGTH_SHORT).show();
    }

    private void markAsVerified() {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(documentId)) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ChronicCarePrefs", MODE_PRIVATE);
        String reviewerName = prefs.getString("userName", "Doctor");
        Map<String, Object> review = new HashMap<>();
        review.put("status", "approved");
        review.put("reviewedAt", System.currentTimeMillis());
        review.put("reviewerName", reviewerName);

        firestore.collection("users").document(userId)
            .collection("documents").document(documentId)
            .update("review", review);

        Toast.makeText(this, "Report verified", Toast.LENGTH_SHORT).show();
    }

    private void openReport() {
        if (documentUrl == null || documentUrl.trim().isEmpty()) {
            Toast.makeText(this, "Report URL unavailable", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentUrl));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open report", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, Object> buildProfileMap(
            DocumentSnapshot personalDoc,
            DocumentSnapshot medicalDoc,
            DocumentSnapshot emergencyDoc,
            User localUser
    ) {
        Map<String, Object> profile = new HashMap<>();
        putIfPresent(profile, "name", getValue(personalDoc, "name", localUser != null ? localUser.name : null));
        putIfPresent(profile, "email", getValue(personalDoc, "email", localUser != null ? localUser.email : null));
        putIfPresent(profile, "phone", getValue(personalDoc, "phone", localUser != null ? localUser.phone : null));
        putIfPresent(profile, "dob", getValue(personalDoc, "dob", localUser != null ? localUser.dob : null));
        putIfPresent(profile, "gender", getValue(personalDoc, "gender", localUser != null ? localUser.gender : null));
        putIfPresent(profile, "bloodGroup", getValue(personalDoc, "bloodGroup", localUser != null ? localUser.bloodGroup : null));
        putIfPresent(profile, "height", getValue(medicalDoc, "height", localUser != null ? localUser.height : null));
        putIfPresent(profile, "weight", getValue(medicalDoc, "weight", localUser != null ? localUser.weight : null));
        putIfPresent(profile, "conditions", getValue(medicalDoc, "conditions", localUser != null ? localUser.conditions : null));
        putIfPresent(profile, "allergies", getValue(medicalDoc, "allergies", localUser != null ? localUser.allergies : null));
        putIfPresent(profile, "emergencyName", getValue(emergencyDoc, "name", localUser != null ? localUser.emergencyName : null));
        putIfPresent(profile, "emergencyPhone", getValue(emergencyDoc, "phone", localUser != null ? localUser.emergencyPhone : null));
        putIfPresent(profile, "emergencyRelation", getValue(emergencyDoc, "relation", localUser != null ? localUser.emergencyRelation : null));
        return profile;
    }

    private List<Map<String, Object>> buildMedicationList(QuerySnapshot medsSnapshot) {
        List<Map<String, Object>> meds = new ArrayList<>();
        if (medsSnapshot == null) {
            return meds;
        }
        for (DocumentSnapshot doc : medsSnapshot.getDocuments()) {
            Map<String, Object> entry = new HashMap<>();
            String name = doc.getString("name");
            if (name == null || name.trim().isEmpty()) {
                continue;
            }
            entry.put("name", name);
            putIfPresent(entry, "time", doc.getString("time"));
            putIfPresent(entry, "mealTime", doc.getString("mealTime"));
            putIfPresent(entry, "period", doc.getString("period"));
            entry.put("taken", doc.getBoolean("taken"));
            meds.add(entry);
        }
        return meds;
    }

    private void renderList(LinearLayout container, List<String> items, String emptyText) {
        container.removeAllViews();
        if (items == null || items.isEmpty()) {
            if (emptyText == null || emptyText.isEmpty()) {
                return;
            }
            TextView textView = createLineView("• " + emptyText);
            container.addView(textView);
            return;
        }
        for (String item : items) {
            container.addView(createLineView("• " + item));
        }
    }

    private void renderSources(LinearLayout container, List<Map<String, Object>> sources) {
        container.removeAllViews();
        if (sources == null || sources.isEmpty()) {
            TextView textView = createLineView("• No sources yet.");
            container.addView(textView);
            return;
        }
        for (Map<String, Object> source : sources) {
            String title = getStringValue(source.get("title"), "Source");
            String snippet = getStringValue(source.get("snippet"), "");
            String url = getStringValue(source.get("url"), "");

            TextView textView = createLineView("• " + title + (snippet.isEmpty() ? "" : "\n  " + snippet));
            if (!url.isEmpty()) {
                textView.setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception ignored) {}
                });
            }
            container.addView(textView);
        }
    }

    private TextView createLineView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTextColor(0xFF333333);
        textView.setPadding(0, 4, 0, 4);
        return textView;
    }

    private Map<String, Object> safeMap(Object data) {
        if (data instanceof Map) {
            //noinspection unchecked
            return (Map<String, Object>) data;
        }
        return new HashMap<>();
    }

    private List<String> toStringList(Object data) {
        if (data instanceof List) {
            List<?> raw = (List<?>) data;
            List<String> result = new ArrayList<>();
            for (Object item : raw) {
                if (item != null) {
                    result.add(String.valueOf(item));
                }
            }
            return result;
        }
        return null;
    }

    private List<Map<String, Object>> toMapList(Object data) {
        if (data instanceof List) {
            List<?> raw = (List<?>) data;
            List<Map<String, Object>> result = new ArrayList<>();
            for (Object item : raw) {
                if (item instanceof Map) {
                    //noinspection unchecked
                    result.add((Map<String, Object>) item);
                }
            }
            return result;
        }
        return null;
    }

    private String getStringValue(Object value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? fallback : text;
    }

    private Double getDoubleValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }

    private String getValue(DocumentSnapshot doc, String key, String fallback) {
        String value = doc != null ? doc.getString(key) : null;
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value;
    }

    private void putIfPresent(Map<String, Object> map, String key, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        map.put(key, value.trim());
    }
}
