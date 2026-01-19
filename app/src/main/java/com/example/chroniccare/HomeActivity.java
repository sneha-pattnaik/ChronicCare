package com.example.chroniccare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends BottomNavActivity {

    // Header
    private TextView mainPageGreeting, mainPageName, mainPageDate;

    // Live data
    private TextView currentReadingValue, lastCheckedTime;

    // Medication
    private TextView nextMedicationName, medTiming;

    // Buttons (FIXED TYPES)
    private AppCompatButton btnCheckNow, btnTakeNow;

    // Quick actions (CardView in XML)
    private CardView logFoodCard, logExerciseCard, viewReportsCard, contactDoctorCard;

    // Logic
    private Calendar nextMedicationTime;
    private Random random;
    private GoogleSignInAccount account;

    private static final String NEXT_MED_NAME = "Empagliflozin 10mg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ REQUIRED
        setContentView(R.layout.activity_main);

        initializeViews();

        account = GoogleSignIn.getLastSignedInAccount(this);
        Log.d("AUTH_DEBUG", "Account null? " + (account == null));

        updateGreeting();
        updateDate();
        updateUserName(account);
        updateInitialReadings();
        updateNextMedication();

        setupClickListeners();
    }

    private void initializeViews() {

        mainPageGreeting = findViewById(R.id.mainPageGreeting);
        mainPageName = findViewById(R.id.mainPageName);
        mainPageDate = findViewById(R.id.mainPageDate);

        currentReadingValue = findViewById(R.id.currentReadingValue);
        lastCheckedTime = findViewById(R.id.LastCheckedTime);

        nextMedicationName = findViewById(R.id.NextMedicationName);
        medTiming = findViewById(R.id.MedTiming);

        // ✅ FIXED
        btnCheckNow = findViewById(R.id.btn_checknow);
        btnTakeNow  = findViewById(R.id.btn_takenow);

        logFoodCard = findViewById(R.id.LogFood);
        logExerciseCard = findViewById(R.id.LogExercise);
        viewReportsCard = findViewById(R.id.ViewReports);
        contactDoctorCard = findViewById(R.id.ContactDoctor);

        random = new Random();

        nextMedicationTime = Calendar.getInstance();
        nextMedicationTime.add(Calendar.MINUTE, 15);
    }

    private void updateGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) mainPageGreeting.setText("Good Morning,");
        else if (hour < 17) mainPageGreeting.setText("Good Afternoon,");
        else mainPageGreeting.setText("Good Evening,");
    }

    private void updateDate() {
        String date = new SimpleDateFormat(
                "EEEE, MMM dd",
                Locale.getDefault()
        ).format(Calendar.getInstance().getTime());
        mainPageDate.setText(date);
    }

    private void updateUserName(GoogleSignInAccount account) {

        String fullName = "User";

        if (account != null) {
            if (account.getDisplayName() != null)
                fullName = account.getDisplayName();
            else if (account.getGivenName() != null)
                fullName = account.getGivenName();
            else if (account.getEmail() != null)
                fullName = account.getEmail();
        }

        String displayName = formatName(fullName);
        mainPageName.setText(displayName);
    }
    private String formatName(String name) {

        if (name == null || name.trim().isEmpty())
            return "User";

        name = name.trim();

        // If there is no space, return as-is
        if (!name.contains(" "))
            return name;

        // Extract first name
        String firstName = name.substring(0, name.indexOf(" "));

        // If name is longer than first name, add dots
        return firstName + "...";
    }


    private void updateInitialReadings() {
        currentReadingValue.setText("128");
        updateLastCheckedTime();
    }

    private void updateLastCheckedTime() {
        String time = new SimpleDateFormat(
                "h:mma",
                Locale.getDefault()
        ).format(Calendar.getInstance().getTime()).toLowerCase();
        lastCheckedTime.setText("Last checked at " + time);
    }

    private void updateNextMedication() {
        nextMedicationName.setText(NEXT_MED_NAME);
        long minutes =
                (nextMedicationTime.getTimeInMillis() - System.currentTimeMillis()) / 60000;
        medTiming.setText(minutes <= 0
                ? "Due Now -"
                : "Due In " + minutes + " Mins -");
    }

    private void setupClickListeners() {

        btnCheckNow.setOnClickListener(v -> {
            int reading = 80 + random.nextInt(120);
            currentReadingValue.setText(String.valueOf(reading));
            updateLastCheckedTime();
            Toast.makeText(this, "Reading logged", Toast.LENGTH_SHORT).show();
        });

        btnTakeNow.setOnClickListener(v -> {
            nextMedicationTime = Calendar.getInstance();
            nextMedicationTime.add(Calendar.HOUR, 4);
            updateNextMedication();
            Toast.makeText(this, "Medication taken", Toast.LENGTH_SHORT).show();
        });

        logExerciseCard.setOnClickListener(v ->
                startActivity(new Intent(this, LogExercise.class)));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_home;
    }


}
