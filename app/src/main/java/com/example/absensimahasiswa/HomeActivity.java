package com.example.absensimahasiswa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class HomeActivity extends AppCompatActivity {

    private Button btnKehadiran, btnSakit, btnIzin;
    private FusedLocationProviderClient fusedLocationClient;

    // Ganti dengan koordinat kampusmu
    private final double TARGET_LATITUDE = 1.2044341;
    private final double TARGET_LONGITUDE = 124.7917969;
    private final float LOCATION_RADIUS_METERS = 1000; // misalnya 2000 meter = 2 km

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi tombol
        btnKehadiran = findViewById(R.id.btnKehadiran);
        btnSakit = findViewById(R.id.btnSakit);
        btnIzin = findViewById(R.id.btnIzin);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Minta permission lokasi jika perlu
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        checkLocationAndUpdateButtons();
                    } else {
                        Toast.makeText(this, "Izin lokasi ditolak.", Toast.LENGTH_LONG).show();
                        updateButtons(false);
                    }
                });

        // Event klik Kehadiran
        btnKehadiran.setOnClickListener(v -> {
            if (btnKehadiran.isEnabled()) {
                startActivity(new Intent(HomeActivity.this, KehadiranActivity.class));
            } else {
                Toast.makeText(this, "Anda tidak berada di lokasi kampus.", Toast.LENGTH_SHORT).show();
            }
        });

        // Event klik Sakit
        btnSakit.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, Sakit.class));
        });

        // Event klik Izin
        btnIzin.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, Izin.class));
        });

        // Bottom Navigation
        setupBottomNavigation();

        // Cek lokasi saat dibuka
        checkLocationAndUpdateButtons();
    }

    private void checkLocationAndUpdateButtons() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null && isInTargetLocation(location)) {
                            updateButtons(true);
                        } else {
                            updateButtons(false);
                            Toast.makeText(this, "Lokasi Anda di luar area kampus.", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        updateButtons(false);
                        Toast.makeText(this, "Gagal mendapatkan lokasi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            updateButtons(false);
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private boolean isInTargetLocation(@NonNull Location location) {
        float[] results = new float[1];
        Location.distanceBetween(
                location.getLatitude(),
                location.getLongitude(),
                TARGET_LATITUDE,
                TARGET_LONGITUDE,
                results);
        return results[0] <= LOCATION_RADIUS_METERS;
    }

    private void updateButtons(boolean enableKehadiran) {
        btnKehadiran.setEnabled(enableKehadiran);
        btnSakit.setEnabled(true);
        btnIzin.setEnabled(true);
    }

    private void setupBottomNavigation() {
        ImageView navProfile = findViewById(R.id.nav_profil);
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navList = findViewById(R.id.nav_history);

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfilActivity.class));
            finish();
        });

        navHome.setOnClickListener(v -> {
            // Sudah di home
        });

        navList.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, RiwayatActivity.class));
            finish();
        });
    }
}
