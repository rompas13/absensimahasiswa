package com.example.absensimahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KehadiranActivity extends AppCompatActivity {

    private EditText editTextNamaMahasiswa;
    private EditText editTextNIM;
    private EditText editTextMataKuliah;
    private EditText editTextTanggalMasuk;
    private EditText editTextJamMasuk;
    private Button buttonHadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran);

        // Initialize views
        editTextNamaMahasiswa = findViewById(R.id.edit_text_nama_mahasiswa);
        editTextNIM = findViewById(R.id.edit_text_nim);
        editTextMataKuliah = findViewById(R.id.edit_text_mata_kuliah);
        editTextTanggalMasuk = findViewById(R.id.edit_text_tanggal_masuk);
        editTextJamMasuk = findViewById(R.id.edit_text_jam_masuk);
        buttonHadir = findViewById(R.id.button_hadir);

        // Set current date and time
        setCurrentDateTime();

        // Set up hadir button click listener
        buttonHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAttendance();
            }
        });

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void setCurrentDateTime() {
        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        editTextTanggalMasuk.setText(currentDate);

        // Get current time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        editTextJamMasuk.setText(currentTime);
    }

    private void submitAttendance() {
        // Get input values
        String namaMahasiswa = editTextNamaMahasiswa.getText().toString().trim();
        String nim = editTextNIM.getText().toString().trim();
        String mataKuliah = editTextMataKuliah.getText().toString().trim();
        String tanggalMasuk = editTextTanggalMasuk.getText().toString().trim();
        String jamMasuk = editTextJamMasuk.getText().toString().trim();

        // Validate inputs
        if (namaMahasiswa.isEmpty() || nim.isEmpty() || mataKuliah.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would typically save the attendance data to database
        // For now, just show a success message
        Toast.makeText(this, "Kehadiran berhasil dicatat", Toast.LENGTH_SHORT).show();

        // Optional: Clear the form or navigate to another screen
        clearForm();
    }

    private void clearForm() {
        editTextNamaMahasiswa.setText("");
        editTextNIM.setText("");
        editTextMataKuliah.setText("");
        // Reset date and time to current
        setCurrentDateTime();
    }

    private void setupBottomNavigation() {
        // Find bottom navigation views
        ImageView navProfile = findViewById(R.id.nav_profil);
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navHistory = findViewById(R.id.nav_history);

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile activity
                Intent intent = new Intent(KehadiranActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home activity
                Intent intent = new Intent(KehadiranActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        navHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to History/List activity
                Intent intent = new Intent(KehadiranActivity.this, RiwayatActivity.class);
                startActivity(intent);
            }
        });
    }
}