package com.example.absensimahasiswa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Izin extends AppCompatActivity {

    private TextView tvNamaMahasiswa, tvNim, tvMatakuliah;
    private EditText etTanggal, etAlasan;
    private Button btnUpload, btnKirim;
    private ImageView btnKembali, imagePreview;

    private Uri fileUri = null;
    private ActivityResultLauncher<Intent> fileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin);

        tvNamaMahasiswa = findViewById(R.id.tv_nama_mahasiswa);
        tvNim = findViewById(R.id.tv_nim);
        tvMatakuliah = findViewById(R.id.tv_matakuliah);
        etTanggal = findViewById(R.id.edit_text_tanggal);
        etAlasan = findViewById(R.id.edit_text_keterangan);
        btnUpload = findViewById(R.id.btn_upload_foto);
        btnKirim = findViewById(R.id.button_izin);
        btnKembali = findViewById(R.id.btn_kembali);
        imagePreview = findViewById(R.id.image_bukti);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String nama = prefs.getString("nama", "");
        String nim = prefs.getString("nim", "");
        tvNamaMahasiswa.setText(nama);
        tvNim.setText(nim);

        String matkul = "Workshop Mobile Programming (Sulastri Eksan, ST., MT)";
        tvMatakuliah.setText(matkul);

        etTanggal.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker view, int year, int month, int day) -> {
                etTanggal.setText(String.format("%04d-%02d-%02d", year, month + 1, day));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        fileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        fileUri = result.getData().getData();
                        imagePreview.setImageURI(fileUri);
                        imagePreview.setVisibility(ImageView.VISIBLE);
                        Toast.makeText(this, "Foto berhasil dipilih!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            fileLauncher.launch(Intent.createChooser(intent, "Pilih bukti izin"));
        });

        btnKirim.setOnClickListener(v -> {
            String tanggal = etTanggal.getText().toString().trim();
            String alasan = etAlasan.getText().toString().trim();

            if (tanggal.isEmpty() || alasan.isEmpty() || fileUri == null) {
                Toast.makeText(this, "Harap isi semua data dan unggah foto!", Toast.LENGTH_SHORT).show();
                return;
            }

            kirimDataDenganFoto(nim, nama, tanggal, alasan, matkul, fileUri);
        });

        btnKembali.setOnClickListener(v -> finish());
    }

    private void kirimDataDenganFoto(String nim, String nama, String tanggal, String alasan, String matkul, Uri uri) {
        String url = "http://192.168.28.22/andro/input_izin.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(Izin.this, "Pengajuan izin berhasil dikirim!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String msg = (error.getMessage() != null) ? error.getMessage() : "Gagal mengirim data!";
                    Toast.makeText(Izin.this, msg, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nim);
                params.put("nama", nama);
                params.put("tanggal", tanggal);
                params.put("alasan", alasan);
                params.put("matkul", matkul);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> byteData = new HashMap<>();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    byte[] imageData = bos.toByteArray();
                    byteData.put("bukti", new DataPart("bukti.jpg", imageData, "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return byteData;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
