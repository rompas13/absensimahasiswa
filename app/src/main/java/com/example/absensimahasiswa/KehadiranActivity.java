package com.example.absensimahasiswa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KehadiranActivity extends AppCompatActivity {

    EditText txtNama, txtNIM, txtMatkul, txtTanggal, txtJam;
    Button btnSubmit, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran);

        txtNama = findViewById(R.id.text_view_nama_mahasiswa);
        txtNIM = findViewById(R.id.text_view_nim);
        txtMatkul = findViewById(R.id.edit_text_mata_kuliah);
        txtTanggal = findViewById(R.id.edit_text_tanggal_masuk);
        txtJam = findViewById(R.id.edit_text_jam_masuk);
        btnSubmit = findViewById(R.id.button_hadir);
        btnBack = findViewById(R.id.button_back);

        // Setelah itu baru ambil SharedPreferences dan set text
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        txtNama.setText(preferences.getString("nama", ""));
        txtNIM.setText(preferences.getString("nim", ""));
        txtMatkul.setText(preferences.getString("matkul", ""));



        // DatePicker
        Calendar calendar = Calendar.getInstance();
        txtTanggal.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(KehadiranActivity.this, (view, year, month, dayOfMonth) -> {
                txtTanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // TimePicker
        txtJam.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(KehadiranActivity.this, (view, hourOfDay, minute1) -> {
                String formattedMinute = minute1 < 10 ? "0" + minute1 : String.valueOf(minute1);
                String formattedHour = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                txtJam.setText(formattedHour + ":" + formattedMinute);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        btnSubmit.setOnClickListener(view -> {
            String nama = txtNama.getText().toString().trim();
            String nim = txtNIM.getText().toString().trim();
            String matkul = txtMatkul.getText().toString().trim();
            String tanggalInput = txtTanggal.getText().toString().trim();
            String jam = txtJam.getText().toString().trim();
            String status = "Hadir";

            if (nama.isEmpty() || nim.isEmpty() || matkul.isEmpty() || tanggalInput.isEmpty() || jam.isEmpty()) {
                Toast.makeText(KehadiranActivity.this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat fromUser = new SimpleDateFormat("d/M/yyyy");
                SimpleDateFormat mySqlFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalFormatted = "";

                try {
                    Date date = fromUser.parse(tanggalInput);
                    tanggalFormatted = mySqlFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(KehadiranActivity.this, "Format tanggal salah!", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertKehadiran(nama, nim, matkul, jam, tanggalFormatted, status);
            }
        });

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(KehadiranActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void insertKehadiran(String nama, String nim, String matkul, String jam, String tanggal, String status) {
        String url = "http://10.0.2.2/andro/insert_kehadiran.php";

        new Thread(() -> {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("nim", nim);
                params.put("matkul", matkul);
                params.put("jam_hadir", jam);
                params.put("tanggal", tanggal);
                params.put("status", status);

                String response = RequestHandler.sendPostRequest(url, params);

                runOnUiThread(() -> {
                    if (response.equalsIgnoreCase("success")) {
                        Toast.makeText(KehadiranActivity.this, "Kehadiran berhasil dicatat", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(KehadiranActivity.this, "Gagal mencatat kehadiran", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(KehadiranActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
