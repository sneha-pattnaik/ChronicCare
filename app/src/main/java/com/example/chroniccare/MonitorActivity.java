// MonitorActivity.java
package com.example.chroniccare;

import android.os.Bundle;

public class MonitorActivity extends BottomNavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_monitor;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_monitor;
    }
}
