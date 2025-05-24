package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvNama, tvNim;
    private EditText etSemester, etProdi, etJurusan;
    private ImageView navHome, navHistory, navProfil;
    private Button btnKeluar, btnSimpan;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Inisialisasi View
        tvNama = findViewById(R.id.tv_nama);
        tvNim = findViewById(R.id.tv_nim);
        etSemester = findViewById(R.id.et_semester);
        etProdi = findViewById(R.id.et_prodi);
        etJurusan = findViewById(R.id.et_jurusan);

        navHome = findViewById(R.id.nav_home);
        navHistory = findViewById(R.id.nav_history);
        navProfil = findViewById(R.id.nav_profil);
        btnKeluar = findViewById(R.id.btn_keluar);
        btnSimpan = findViewById(R.id.btn_simpan);  // Tombol Simpan

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Ambil data dari SharedPreferences
        String nama = sharedPreferences.getString("nama", "Tidak diketahui");
        String nim = sharedPreferences.getString("nim", "Tidak diketahui");
        String semester = sharedPreferences.getString("semester", "");
        String prodi = sharedPreferences.getString("prodi", "");
        String jurusan = sharedPreferences.getString("jurusan", "");

        // Tampilkan data
        tvNama.setText("Nama: " + nama);
        tvNim.setText("NIM: " + nim);

        etSemester.setText(semester);
        etProdi.setText(prodi);
        etJurusan.setText(jurusan);

        // Pasang filter agar etSemester hanya angka
        etSemester.setFilters(new InputFilter[] { new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        // Tombol Simpan klik: simpan data dan tampilkan Toast
        btnSimpan.setOnClickListener(v -> {
            saveUserInput();
            Toast.makeText(ProfilActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        navHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, RiwayatActivity.class);
            startActivity(intent);
            finish();
        });

        navProfil.setOnClickListener(v -> {
            // Sudah di Profil, tidak perlu action
        });

        btnKeluar.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveUserInput() {
        String semesterInput = etSemester.getText().toString().trim();
        String prodiInput = etProdi.getText().toString().trim();
        String jurusanInput = etJurusan.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("semester", semesterInput);
        editor.putString("prodi", prodiInput);
        editor.putString("jurusan", jurusanInput);
        editor.apply();
    }
}
