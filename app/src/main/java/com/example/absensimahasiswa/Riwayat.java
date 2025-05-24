package com.example.absensimahasiswa;

public class Riwayat {
    private String nama;
    private String nim;        // Ganti dari nik ke nim
    private String tanggal;
    private String status;

    public Riwayat(String nama, String nim, String tanggal, String status) {
        this.nama = nama;
        this.nim = nim;
        this.tanggal = tanggal;
        this.status = status;
    }

    // Getter
    public String getNama() {
        return nama;
    }

    public String getNim() {      // Ganti dari getNik ke getNim
        return nim;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    // Setter (jika diperlukan)
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNim(String nim) {    // Ganti dari setNik ke setNim
        this.nim = nim;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Riwayat{" +
                "nama='" + nama + '\'' +
                ", nim='" + nim + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
