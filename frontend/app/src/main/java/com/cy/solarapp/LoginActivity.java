package com.cy.solarapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private MaterialButton loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String userRoleFromSelect; // This will hold the role passed from RoleSelectActivity

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Get the user role passed from RoleSelectActivity
        userRoleFromSelect = getIntent().getStringExtra("userRole");

        // Initialize Firebase Authentication and Database references
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        // Initialize UI elements
        emailField = findViewById(R.id.userame_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_btn);

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Please enter your email");
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Please enter your password");
            return;
        }


        progressDialog.show();
        // Firebase Authentication to check email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign-in successful, now verify user role
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        } else {
                            progressDialog.dismiss();
                            // If sign-in fails, display a message to the user
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserRole(String userId) {
        progressDialog.show();
        // Reference to the user's data in Firebase Realtime Database
        mDatabase.child("Users").child(userId).child("userRole").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userRole = dataSnapshot.getValue(String.class);
                    if (userRole != null) {
                        // Check if the user role matches the role selected in RoleSelectActivity
                        if (userRole.equals(userRoleFromSelect)) {
                            progressDialog.dismiss();
                            // Proceed to the next activity based on role
                            if (userRole.equals("Admin")) {
                                Intent intent = new Intent(LoginActivity.this, AdminAccountActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (userRole.equals("Client")) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            progressDialog.dismiss();
                            // Role mismatch, show error
                            Toast.makeText(LoginActivity.this, "Role mismatch, please select the correct role.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "User role not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User not found in the database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}