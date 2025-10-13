package com.example.chroniccare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LogExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure you have R.layout.activity_log_exercise.xml
        setContentView(R.layout.activity_log_exercise);

        // Hide the default Action Bar, as your design doesn't show one
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize and set a listener for the "Start Exercise" button
        Button btnStartExercise = findViewById(R.id.btnStartExercise);
        btnStartExercise.setOnClickListener(v -> {
            // Replace this with actual navigation logic (e.g., starting a new intent)
            Toast.makeText(LogExerciseActivity.this, "Starting a new exercise session!", Toast.LENGTH_SHORT).show();
        });

        // NOTE: For the Progress Cards and Exercise List, you would typically use
        // a RecyclerView. Since this is complex setup, we are using <include/>
        // tags in the XML for demonstration, and you would replace those with
        // RecyclerView setup code here.
    }
}