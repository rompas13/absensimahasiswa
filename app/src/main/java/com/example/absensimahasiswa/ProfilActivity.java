package com.example.absensimahasiswa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvNama, tvNim, tvSemester, tvProdi, tvJurusan;
    private Button btnKeluar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Initialize views
        tvNama = findViewById(R.id.tv_nama);
        tvNim = findViewById(R.id.tv_nim);
        tvSemester = findViewById(R.id.tv_semester);
        tvProdi = findViewById(R.id.tv_prodi);
        tvJurusan = findViewById(R.id.tv_jurusan);
        btnKeluar = findViewById(R.id.btn_keluar);

        // Set user data (this would normally come from database or session)
        // For example:
        setUserData("Refrilly", "12345678", "4", "Teknik Elektro", "Teknik Informatika");

        // Set logout button click listener
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic
                logout();
            }
        });

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setUserData(String nama, String nim, String semester, String prodi, String jurusan) {
        tvNama.setText(nama);
        tvNim.setText(nim);
        tvSemester.setText(semester);
        tvProdi.setText(prodi);
        tvJurusan.setText(jurusan);
    }

    private void logout() {
        // Clear user session/data
        // For example:
        // SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
        // editor.clear();
        // editor.apply();

        // Navigate to login screen
        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        // Find bottom navigation views
        ImageView navProfile = findViewById(R.id.nav_profil);
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navHistory = findViewById(R.id.nav_history);

        // Already in Profile activity, highlight profile icon or do nothing
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already in Profile activity, do nothing
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home activity
                Intent intent = new Intent(ProfilActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        navHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to History/List activity
                Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
}