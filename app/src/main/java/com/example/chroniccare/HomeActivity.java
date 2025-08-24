// HomeActivity.java
package com.example.chroniccare;

import android.os.Bundle;

public class HomeActivity extends BottomNavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main; // your page layout
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_home;
    }
}
