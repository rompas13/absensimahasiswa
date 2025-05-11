package com.example.absensimahasiswa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint("MissingInflatedId") TextView textView = findViewById(R.id.textWelcome);
        textView.setText("Selamat datang di halaman utama!");
    }
}