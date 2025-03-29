package com.cy.solarapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class RoleSelectActivity extends AppCompatActivity {

    private MaterialButton clientBtn, adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_role_select);

        clientBtn = findViewById(R.id.client_btn);
        adminBtn = findViewById(R.id.admin_btn);

        clientBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, LoginActivity.class);
            intent.putExtra("userRole", "Client"); // Passing "Client" role
            startActivity(intent);
        });

        adminBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, LoginActivity.class);
            intent.putExtra("userRole", "Admin"); // Passing "Admin" role
            startActivity(intent);
        });
    }

}