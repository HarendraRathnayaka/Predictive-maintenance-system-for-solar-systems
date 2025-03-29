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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ViewClientsActivity extends AppCompatActivity {

    private EditText searchSolarId;
    private TextInputEditText usernameTxt, emailTxt, solarIdTxt;
    private MaterialButton viewSolarDataBtn, removeClientBtn, searchBtn;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String foundUserId = null; // Store found userId

    DrawerLayout drawerLayout;
    ImageView sideMenuIcon;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_clients);
        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuIcon = findViewById(R.id.side_menu_icon);
        navigationView = findViewById(R.id.nav_view);



        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // UI Elements
        searchSolarId = findViewById(R.id.search_solar_id);
        usernameTxt = findViewById(R.id.username_txt);
        emailTxt = findViewById(R.id.email_txt);
        solarIdTxt = findViewById(R.id.solar_id);
        searchBtn = findViewById(R.id.search_btn);

        viewSolarDataBtn = findViewById(R.id.view_solar_data);
        removeClientBtn = findViewById(R.id.remove_client);
        progressDialog = new ProgressDialog(this);

        // Disable input fields initially
        disableInputFields();

        // Search solar ID when user types
        searchBtn.setOnClickListener(view -> {
                String solarId = searchSolarId.getText().toString().trim();
                if (!TextUtils.isEmpty(solarId)) {
                    searchUserBySolarId(solarId);
                }
        });

        // View solar data
        viewSolarDataBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewClientsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Remove client
        removeClientBtn.setOnClickListener(v -> {
            String solarId = searchSolarId.getText().toString().trim();
            if (!TextUtils.isEmpty(solarId)) {
                removeUserBySolarId(solarId);
            } else {
                Toast.makeText(this, "Enter a valid Solar ID", Toast.LENGTH_SHORT).show();
            }
        });

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

//                if (id == R.id.nav_view_clients) {
//                    startActivity(new Intent(ViewClientsActivity.this, ViewClientsActivity.class));
//                } else --- this is active tab

                if (id == R.id.nav_register_client) {
                    startActivity(new Intent(ViewClientsActivity.this, RegisterClientActivity.class));
                } else if (id == R.id.nav_account) {
                    startActivity(new Intent(ViewClientsActivity.this, AdminAccountActivity.class));
                } else if (id == R.id.nav_logout) {
                    finish(); // Log out
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });




    }


    private void searchUserBySolarId(String solarId) {
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        databaseReference.orderByChild("solarId").equalTo(solarId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                foundUserId = snapshot.getKey(); // Get User ID
                                String username = snapshot.child("userName").getValue(String.class);
                                String email = snapshot.child("userEmail").getValue(String.class);
                                String solarId = snapshot.child("solarId").getValue(String.class);

                                usernameTxt.setText(username);
                                emailTxt.setText(email);
                                solarIdTxt.setText(solarId);

                                disableInputFields();
                                return;
                            }
                        } else {
                            foundUserId = null;
                            Toast.makeText(ViewClientsActivity.this, "No user found with this Solar ID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewClientsActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeUserBySolarId(String solarId) {
        if (foundUserId == null) {
            Toast.makeText(this, "User not found. Search for Solar ID first.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Removing user...");
        progressDialog.show();

        // Remove from Firebase Database
        databaseReference.child(foundUserId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Remove from Firebase Authentication
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.getUid().equals(foundUserId)) {
                            user.delete().addOnCompleteListener(authTask -> {
                                progressDialog.dismiss();
                                if (authTask.isSuccessful()) {
                                    Toast.makeText(ViewClientsActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                } else {
                                    Toast.makeText(ViewClientsActivity.this, "Failed to delete user authentication", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ViewClientsActivity.this, "User deleted successfully from database", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ViewClientsActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void disableInputFields() {
        usernameTxt.setEnabled(false);
        emailTxt.setEnabled(false);
        solarIdTxt.setEnabled(false);
    }

    private void clearFields() {
        searchSolarId.setText("");
        usernameTxt.setText("");
        emailTxt.setText("");
        solarIdTxt.setText("");
    }
}