package com.cy.solarapp;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

public class AdminAccountActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView sideMenuIcon;
    NavigationView navigationView;

    private TextInputEditText fullNameEditText, userNameEditText, contactNoEditText;
    private TextInputLayout fullNameLayout, userNameLayout, contactNoLayout;
    private MaterialButton updateBtn;
    private TextView changePwdTextView, emailTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_account);

        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuIcon = findViewById(R.id.side_menu_icon);
        navigationView = findViewById(R.id.nav_view);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        fullNameEditText = findViewById(R.id.name_txt);
        userNameEditText = findViewById(R.id.username_txt);
        contactNoEditText = findViewById(R.id.c_no_txt);
        updateBtn = findViewById(R.id.update_btn);
        changePwdTextView = findViewById(R.id.change_pwd);
        emailTextView = findViewById(R.id.email_txt);


        // Set email field to the logged-in user's email and disable it
        emailTextView.setText(currentUser.getEmail());
        emailTextView.setEnabled(false);  // Prevent editing of email


        // Show information that email cannot be changed
        emailTextView.setOnClickListener(v ->{
            Toast.makeText(AdminAccountActivity.this, "Email cannot be changed.", Toast.LENGTH_LONG).show();
        });

        // Set onClickListener for Update Button
        updateBtn.setOnClickListener(view -> updateUserInfo());

        // Set onClickListener for Change Password TextView
        changePwdTextView.setOnClickListener(view -> sendPasswordResetLink());



        // hide the user nav item and show admin item group
        navigationView.getMenu().setGroupVisible(R.id.admin_menu, true);
        navigationView.getMenu().setGroupVisible(R.id.user_menu, false);

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

//                if (id == R.id.nav_account) {
//                    startActivity(new Intent(AdminAccountActivity.this, AdminAccountActivity.class));
//                } else --- this is active tab

                if (id == R.id.nav_register_client) {
                    startActivity(new Intent(AdminAccountActivity.this, RegisterClientActivity.class));
                } else if (id == R.id.nav_view_clients) {
                    startActivity(new Intent(AdminAccountActivity.this, ViewClientsActivity.class));
                } else if (id == R.id.nav_logout) {
                    finish(); // Log out
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }
    private void updateUserInfo() {
        // Validate inputs
        String fullName = fullNameEditText.getText().toString().trim();
        String userName = userNameEditText.getText().toString().trim();
        String contactNo = contactNoEditText.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(AdminAccountActivity.this, "Full name is required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(userName)) {

            Toast.makeText(AdminAccountActivity.this, "Username is required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(contactNo)) {

            Toast.makeText(AdminAccountActivity.this, "Contact number is required.", Toast.LENGTH_LONG).show();
            return;
        }

        // Reference to the current user's node in Firebase
        DatabaseReference userRef = mDatabase.child("Users").child(userId);

        // Check if the fields exist in the database
        userRef.child("fullName").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Update user data if it exists
                    userRef.child("fullName").setValue(fullName);
                    userRef.child("userName").setValue(userName);
                    userRef.child("contactNo").setValue(contactNo);
                    Toast.makeText(AdminAccountActivity.this, "User information updated", Toast.LENGTH_SHORT).show();
                } else {
                    // Create new data if it doesn't exist
                    userRef.child("fullName").setValue(fullName);
                    userRef.child("userName").setValue(userName);
                    userRef.child("contactNo").setValue(contactNo);
                    Toast.makeText(AdminAccountActivity.this, "User information created", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminAccountActivity.this, "Failed to check user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPasswordResetLink() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminAccountActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminAccountActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
