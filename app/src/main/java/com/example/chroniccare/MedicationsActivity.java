// MonitorActivity.java
package com.example.chroniccare;

import android.os.Bundle;

public class MedicationsActivity extends BottomNavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_medications;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_medications;
    }
}
