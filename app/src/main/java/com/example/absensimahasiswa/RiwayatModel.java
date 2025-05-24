package com.example.absensimahasiswa;

public class RiwayatModel {
    private String nama;
    private String nim;
    private String tanggal;
    private String status;

    public RiwayatModel(String nama, String nim, String tanggal, String status) {
        this.nama = nama;
        this.nim = nim;
        this.tanggal = tanggal;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }
}
