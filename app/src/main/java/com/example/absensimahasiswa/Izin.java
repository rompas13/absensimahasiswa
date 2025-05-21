package com.example.absensimahasiswa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Izin extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 100;
    private EditText editTextTanggal, editTextKeterangan;
    private TextView editTextBukti;
    private ImageView imageBukti;
    private Uri imageUri = null;

    // Variables to store user data
    private String nama, nim, matakuliah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_izin);

        // Initialize views
        initializeViews();

        // Load user data from SharedPreferences and set to UI
        loadAndSetUserData();

        // Setup event listeners
        setupEventListeners();

        // Setup window insets
        setupWindowInsets();
    }

    private void initializeViews() {
        editTextTanggal = findViewById(R.id.edit_text_tanggal);
        editTextKeterangan = findViewById(R.id.edit_text_keterangan);
        editTextBukti = findViewById(R.id.edit_text_bukti);
        imageBukti = findViewById(R.id.image_bukti);

        // Make sure the imageBukti is visible when there's an image
        imageBukti.setVisibility(View.GONE);
    }

    private void loadAndSetUserData() {
        // Ambil data dari SharedPreferences dengan key yang benar
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        nama = preferences.getString("nama", "Nama Tidak Diketahui");
        nim = preferences.getString("nim", "NIM Tidak Diketahui");
        matakuliah = preferences.getString("matkul", "Mata Kuliah Tidak Diketahui"); // Key yang benar: "matkul"

        // Cek apakah user sudah login
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            // Redirect ke login jika belum login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set data ke TextView
        TextView tvNama = findViewById(R.id.tv_nama_mahasiswa);
        TextView tvNim = findViewById(R.id.tv_nim);
        TextView tvMatakuliah = findViewById(R.id.tv_matakuliah);

        tvNama.setText(nama);
        tvNim.setText(nim);
        tvMatakuliah.setText(matakuliah);
    }

    private void setupEventListeners() {
        // Tombol upload foto dan kirim izin
        Button btnUploadFoto = findViewById(R.id.btn_upload_foto);
        Button btnIzin = findViewById(R.id.button_izin);
        ImageView btnKembali = findViewById(R.id.btn_kembali);

        // Bottom Navigation
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navProfil = findViewById(R.id.nav_profil);
        ImageView navHistory = findViewById(R.id.nav_history);

        // Date picker untuk tanggal
        editTextTanggal.setOnClickListener(v -> showDatePicker());

        // Open gallery saat tombol upload foto diklik
        btnUploadFoto.setOnClickListener(v -> openGallery());

        // Kirim izin saat tombol diklik
        btnIzin.setOnClickListener(v -> submitIzin());

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        // Setup navigation
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        navProfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        });

        navHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, RiwayatActivity.class);
            startActivity(intent);
        });
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                    // Format tanggal dengan leading zero untuk konsistensi
                    String tanggalDipilih = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    editTextTanggal.setText(tanggalDipilih);
                }, year, month, day);

        // Set minimum date ke hari ini (tidak bisa pilih tanggal masa lalu)
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    private void submitIzin() {
        String tanggal = editTextTanggal.getText().toString().trim();
        String keterangan = editTextKeterangan.getText().toString().trim();
        String bukti = (imageUri != null) ? imageUri.toString() : "";

        // Validasi input
        if (tanggal.isEmpty()) {
            Toast.makeText(this, "Pilih tanggal izin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (keterangan.isEmpty()) {
            Toast.makeText(this, "Masukkan keterangan izin!", Toast.LENGTH_SHORT).show();
            editTextKeterangan.requestFocus();
            return;
        }

        if (bukti.isEmpty()) {
            Toast.makeText(this, "Upload bukti foto!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Data sudah lengkap, siap dikirim ke server
        // TODO: Implementasi pengiriman ke server/database
        sendIzinToServer(tanggal, keterangan, bukti);
    }

    private void sendIzinToServer(String tanggal, String keterangan, String bukti) {
        // TODO: Implementasi AsyncTask atau Volley untuk kirim data ke server
        // Data yang akan dikirim:
        // - nim (dari SharedPreferences)
        // - nama (dari SharedPreferences)
        // - matakuliah (dari SharedPreferences)
        // - tanggal (dari input user)
        // - keterangan (dari input user)
        // - bukti foto (imageUri)

        // Sementara ini hanya simulasi
        Toast.makeText(this, "Izin berhasil dikirim!\n" +
                "NIM: " + nim + "\n" +
                "Nama: " + nama + "\n" +
                "Tanggal: " + tanggal, Toast.LENGTH_LONG).show();

        // Reset form setelah berhasil kirim
        resetForm();
    }

    private void resetForm() {
        editTextTanggal.setText("");
        editTextKeterangan.setText("");
        editTextBukti.setText("Belum ada foto dipilih");
        imageBukti.setImageResource(android.R.color.transparent);
        imageBukti.setVisibility(View.GONE);
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                try {
                    // Tampilkan nama file di editTextBukti
                    String filename = getFileNameFromUri(imageUri);
                    editTextBukti.setText(filename != null ? filename : "Foto terpilih");

                    // Tampilkan gambar di imageBukti
                    imageBukti.setImageURI(imageUri);
                    imageBukti.setVisibility(View.VISIBLE);

                    Toast.makeText(this, "Foto berhasil dipilih", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error memuat foto", Toast.LENGTH_SHORT).show();
                    imageUri = null;
                }
            }
        }
    }

    // Helper method untuk get filename dari URI
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                fileName = "foto_" + System.currentTimeMillis() + ".jpg";
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }
}