package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Riwayat> riwayatList;
    private RiwayatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        View rootView = findViewById(R.id.root_layout);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> insets);
        }

        recyclerView = findViewById(R.id.recyclerViewRiwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        riwayatList = new ArrayList<>();
        adapter = new RiwayatAdapter(riwayatList);
        recyclerView.setAdapter(adapter);

        // ✅ Ambil SharedPreferences dari "UserData" agar cocok dengan LoginActivity
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String nim = sharedPreferences.getString("nim", null);

        if (nim != null && !nim.isEmpty()) {
            Log.d("RiwayatDebug", "NIM dari SharedPreferences: " + nim);
            ambilDataRiwayat(nim);
        } else {
            Toast.makeText(this, "Data login tidak ditemukan!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navProfil = findViewById(R.id.nav_profil);
        ImageView navHistory = findViewById(R.id.nav_history);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        navProfil.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfilActivity.class));
            finish();
        });
    }

    private void ambilDataRiwayat(String nim) {
        String url = "http://192.168.28.22/andro/get_riwayat.php"; // Ganti IP jika perlu

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            JSONArray array = jsonResponse.getJSONArray("data");
                            riwayatList.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                riwayatList.add(new Riwayat(
                                        obj.getString("nama"),
                                        obj.getString("nim"),
                                        obj.getString("tanggal"),
                                        obj.getString("status")
                                ));
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            String pesan = jsonResponse.getString("message");
                            Toast.makeText(this, "Gagal: " + pesan, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing data JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nim);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
