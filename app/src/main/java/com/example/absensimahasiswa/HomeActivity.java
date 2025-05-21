package com.example.absensimahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnKehadiran;
    private Button btnSakit;
    private Button btnIzin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        btnKehadiran = findViewById(R.id.btnKehadiran);
        btnSakit = findViewById(R.id.btnSakit);
        btnIzin = findViewById(R.id.btnIzin);

        // Set click listeners for each button
        btnKehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Kehadiran activity
                Intent intent = new Intent(HomeActivity.this, KehadiranActivity.class);
                startActivity(intent);
            }
        });

        btnSakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Sakit activity
                Intent intent = new Intent(HomeActivity.this, Sakit.class);
                startActivity(intent);
            }
        });

        btnIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Izin activity
                Intent intent = new Intent(HomeActivity.this, Izin.class);
                startActivity(intent);
            }
        });

        // Bottom navigation setup
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        ImageView navProfile = findViewById(R.id.nav_profil);
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navList = findViewById(R.id.nav_history);

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfilActivity.class); // UBAH KE ProfilActivity
                startActivity(intent);
                finish(); // opsional, agar tidak kembali ke HomeActivity saat back
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sudah berada di HomeActivity, tidak perlu melakukan apa-apa
            }
        });

        navList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RiwayatActivity.class); // UBAH KE RiwayatActivity
                startActivity(intent);
                finish(); // opsional
            }
        });
    }
}