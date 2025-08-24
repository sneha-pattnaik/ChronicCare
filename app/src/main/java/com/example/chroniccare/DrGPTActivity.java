// MonitorActivity.java
package com.example.chroniccare;

import android.os.Bundle;

public class DrGPTActivity extends BottomNavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dr_gptactivity;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_dr_gpt;
    }
}
