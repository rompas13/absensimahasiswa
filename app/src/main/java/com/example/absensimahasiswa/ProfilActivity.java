package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvNama, tvNim, tvSemester, tvProdi, tvJurusan;
    private Button btnKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Inisialisasi View
        tvNama = findViewById(R.id.tv_nama);
        tvNim = findViewById(R.id.tv_nim);
        tvSemester = findViewById(R.id.tv_semester);
        tvProdi = findViewById(R.id.tv_prodi);
        tvJurusan = findViewById(R.id.tv_jurusan);
        btnKeluar = findViewById(R.id.btn_keluar);

        // Ambil nama dan nim dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String nama = preferences.getString("nama", "Nama Tidak Diketahui");
        String nim = preferences.getString("nim", "NIM Tidak Diketahui");

        // Isi manual untuk semester, prodi, jurusan
        String semester = "4";
        String prodi = "Teknik Elektro";
        String jurusan = "Teknik Informatika";

        // Set data ke TextView
        setUserData(nama, nim, semester, prodi, jurusan);

        btnKeluar.setOnClickListener(v -> logout());

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
        // Bersihkan data user di SharedPreferences saat logout
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        // Pindah ke LoginActivity dan hapus riwayat activity
        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        ImageView navProfile = findViewById(R.id.nav_profil);
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navHistory = findViewById(R.id.nav_history);

        navProfile.setOnClickListener(v -> {
            // Sudah di profil, tidak perlu action
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        navHistory.setOnClickListener(v -> {
            // Misal ke HistoryActivity, bukan ProfilActivity lagi
            Intent intent = new Intent(ProfilActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });
    }
}
