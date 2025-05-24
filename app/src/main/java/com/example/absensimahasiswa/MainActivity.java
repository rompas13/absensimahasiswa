package com.example.absensimahasiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Contoh pengambilan data setelah login berhasil (misal dari intent)
        String nim = getIntent().getStringExtra("nim");
        String nama = getIntent().getStringExtra("nama");

        // Simpan ke SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nim", nim);
        editor.putString("nama", nama);
        editor.apply();

        // Tampilkan ke layar
        TextView textView = findViewById(R.id.textWelcome);
        textView.setText("Selamat datang, " + nama + "!");
    }
}
