package com.cy.solarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView sideMenuIcon;
    NavigationView navigationView;
    private LinearLayout mFaultdetectionBtn;
    private LinearLayout mMaintenanceschedulingsystemBtn;
    private LinearLayout mPoweroutputpredictionBtn;
    private LinearLayout mFaultpredictionbysensorBtn;

    private LinearLayout realtimemoniter_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuIcon = findViewById(R.id.side_menu_icon);
        navigationView = findViewById(R.id.nav_view);

        mFaultdetectionBtn = findViewById(R.id.FaultDetection_btn);
        mMaintenanceschedulingsystemBtn = findViewById(R.id.Maintenanceschedulingsystem_btn);
        mPoweroutputpredictionBtn = findViewById(R.id.Poweroutputprediction_btn);
        mFaultpredictionbysensorBtn = findViewById(R.id.FaultPredictionbysensor_btn);
        realtimemoniter_btn = findViewById(R.id.realtimemoniter_btn);

        mFaultdetectionBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FaultDetectionActivity.class));
        });

        realtimemoniter_btn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RealtimeActivity.class));
        });

        mMaintenanceschedulingsystemBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        });

        mFaultpredictionbysensorBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FaultPredictionActivity.class));
        });

        mPoweroutputpredictionBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SolarPanelActivity.class));
        });


        // hide the user nav item and show admin item group
        navigationView.getMenu().setGroupVisible(R.id.admin_menu, false);
        navigationView.getMenu().setGroupVisible(R.id.user_menu, true);

        // Open Drawer when click menu icon
        sideMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });
// Sidebar Menu Click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

//                if (id == R.id.nav_user_main_menu) {
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                } else --- this is active tab

                if (id == R.id.nav_user_account) {
                    startActivity(new Intent(MainActivity.this, UserAccountActivity.class));
                } else if (id == R.id.nav_user_current_analysis) {
                    startActivity(new Intent(MainActivity.this, CurrentAnalyticsActivity.class));
                }else if (id == R.id.nav_user_fault_detection) {
                    startActivity(new Intent(MainActivity.this, FaultDetectionActivity.class));
                } else if (id == R.id.nav_user_maintenance_scheduling) {
                    startActivity(new Intent(MainActivity.this, MaintenanceSchedulingActivity.class));
                } else if (id == R.id.nav_user_power_prediction) {
                    startActivity(new Intent(MainActivity.this, PowerPredictionActivity.class));
                } else if (id == R.id.nav_fault_prediction) {
                    startActivity(new Intent(MainActivity.this, FaultPredictionActivity.class));
                } else if (id == R.id.nav_logout) {
                    finish(); // Log out
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });


        // bottom navigation code

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set the default selected item to "home"
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // Set listener for item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuid = menuItem.getItemId();

                if (menuid == R.id.navigation_home) {
                    return true;
                } else if (menuid == R.id.navigation_about) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                    return true;
                } else if (menuid == R.id.navigation_setting) {
                    startActivity(new Intent(MainActivity.this, MaintenanceSchedulingActivity.class));

                    return true;
                }
                return false;
            }
        });
    }

    private void initView() {

    }
}
