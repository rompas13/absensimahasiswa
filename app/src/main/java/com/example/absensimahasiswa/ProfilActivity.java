package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        btnSimpan = findViewById(R.id.btn_simpan);

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

        // Filter hanya angka untuk semester
        etSemester.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }});

        btnSimpan.setOnClickListener(v -> {
            saveUserInput(); // Simpan lokal
            String nimFix = sharedPreferences.getString("nim", "");
            String semesterFix = etSemester.getText().toString().trim();
            String prodiFix = etProdi.getText().toString().trim();
            String jurusanFix = etJurusan.getText().toString().trim();

            updateProfilToServer(nimFix, semesterFix, prodiFix, jurusanFix); // Simpan server
        });

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfilActivity.this, HomeActivity.class));
            finish();
        });

        navHistory.setOnClickListener(v -> {
            startActivity(new Intent(ProfilActivity.this, RiwayatActivity.class));
            finish();
        });

        navProfil.setOnClickListener(v -> {
            // Sudah di profil
        });

        btnKeluar.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveUserInput() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("semester", etSemester.getText().toString().trim());
        editor.putString("prodi", etProdi.getText().toString().trim());
        editor.putString("jurusan", etJurusan.getText().toString().trim());
        editor.apply();
    }
    private void updateProfilToServer(String nim, String semester, String prodi, String jurusan) {
        String url = "http://192.168.28.22/andro/update_profil.php"; // Ganti IP jika pakai HP

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(ProfilActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(ProfilActivity.this, "Gagal parsing respon server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ProfilActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nim);
                params.put("semester", semester);
                params.put("prodi", prodi);
                params.put("jurusan", jurusan);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
