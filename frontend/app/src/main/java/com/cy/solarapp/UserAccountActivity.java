package com.cy.solarapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccountActivity extends AppCompatActivity {

    private TextInputEditText etUserName, etUserEmail, etSolarId;
    private TextView changePwd;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    DrawerLayout drawerLayout;
    ImageView sideMenuIcon;
    NavigationView navigationView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_account);


        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading data...");
        progressDialog.setCancelable(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuIcon = findViewById(R.id.side_menu_icon);
        navigationView = findViewById(R.id.nav_view);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users"); //

        // Initialize UI components
        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etSolarId = findViewById(R.id.etSolarId);
        changePwd = findViewById(R.id.change_pwd);

        // Disable input fields
        etUserEmail.setEnabled(false);
        etUserName.setEnabled(false);
        etSolarId.setEnabled(false);

        // Load user data
        loadUserData();

        // Handle password reset
        changePwd.setOnClickListener(view -> sendPasswordResetEmail());


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

//                if (id == R.id.nav_user_account) {
//                    startActivity(new Intent(UserAccountActivity.this, UserAccountActivity.class));
//                } else --- this is active tab

                if (id == R.id.nav_user_main_menu) {
                    startActivity(new Intent(UserAccountActivity.this, MainActivity.class));
                } else if (id == R.id.nav_user_current_analysis) {
                    startActivity(new Intent(UserAccountActivity.this, CurrentAnalyticsActivity.class));
                }else if (id == R.id.nav_user_fault_detection) {
                    startActivity(new Intent(UserAccountActivity.this, FaultDetectionActivity.class));
                } else if (id == R.id.nav_user_maintenance_scheduling) {
                    startActivity(new Intent(UserAccountActivity.this, MaintenanceSchedulingActivity.class));
                } else if (id == R.id.nav_user_power_prediction) {
                    startActivity(new Intent(UserAccountActivity.this, PowerPredictionActivity.class));
                } else if (id == R.id.nav_fault_prediction) {
                    startActivity(new Intent(UserAccountActivity.this, FaultPredictionActivity.class));
                } else if (id == R.id.nav_logout) {
                    finish(); // Log out
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }
    private void loadUserData() {
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            etUserEmail.setText(userEmail);

            // Get user ID
            String userId = user.getUid();

            // Fetch user details from Realtime Database
            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressDialog.dismiss();
                    if (snapshot.exists()) {
                        String userName = snapshot.child("userName").getValue(String.class);
                        String solarId = snapshot.child("solarId").getValue(String.class);

                        etUserName.setText(userName);
                        etSolarId.setText(solarId);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(UserAccountActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(UserAccountActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }
}
