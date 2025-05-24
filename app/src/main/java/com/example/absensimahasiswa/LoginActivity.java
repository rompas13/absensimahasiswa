package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText editNamaMahasiswa, editNIM, editPassword;
    private Spinner spinnerMataKuliah;
    private Button buttonMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNamaMahasiswa = findViewById(R.id.editNamaMahasiswa);
        editNIM = findViewById(R.id.editNIM);
        editPassword = findViewById(R.id.editPassword);
        spinnerMataKuliah = findViewById(R.id.spinnerMataKuliah);
        buttonMasuk = findViewById(R.id.buttonMasuk);

        String[] mataKuliahList = {"Pilih Mata Kuliah", "Workshop Mobile Programing (Sulastri Eksan, ST.,MT)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mataKuliahList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMataKuliah.setAdapter(adapter);

        buttonMasuk.setOnClickListener(v -> {
            String nama = editNamaMahasiswa.getText().toString().trim(); // tidak dipakai untuk kirim backend
            String nim = editNIM.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String mataKuliah = spinnerMataKuliah.getSelectedItem().toString();

            if (nim.isEmpty() || password.isEmpty() || mataKuliah.equals("Pilih Mata Kuliah")) {
                Toast.makeText(LoginActivity.this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            } else {
                new LoginTask().execute(nim, password, mataKuliah);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private String nim;

        @Override
        protected String doInBackground(String... params) {
            nim = params[0];
            String password = params[1];
            String mataKuliah = params[2];
            String urlLink = "http://192.168.28.22/andro/AkunMahasiswa.php";

            try {
                URL url = new URL(urlLink);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String postData = "nim=" + URLEncoder.encode(nim, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&mata_kuliah=" + URLEncoder.encode(mataKuliah, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                is.close();
                conn.disconnect();

                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("LoginResponse", result);  // Tambahkan log untuk debugging response JSON

            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("status").equals("success")) {
                    // Ganti nama key sesuai dengan response PHP
                    String nama = obj.getString("nama_mahasiswa");
                    String matkul = obj.getString("mata_kuliah");

                    saveUserData(nim, nama, matkul);

                    Toast.makeText(LoginActivity.this, "Login berhasil! Selamat datang, " + nama, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String pesan = obj.getString("message");
                    Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserData(String nim, String nama, String matkul) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nim", nim);
        editor.putString("nama", nama);
        editor.putString("matkul", matkul);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
}
