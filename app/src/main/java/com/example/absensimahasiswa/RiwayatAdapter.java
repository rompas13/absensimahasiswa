package com.example.absensimahasiswa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private final List<Riwayat> riwayatList;

    public RiwayatAdapter(List<Riwayat> riwayatList) {
        this.riwayatList = riwayatList != null ? riwayatList : new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNama, textNIM, textTanggal, textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNama    = itemView.findViewById(R.id.textNama);
            textNIM     = itemView.findViewById(R.id.text_view_nim);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            textStatus  = itemView.findViewById(R.id.textStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Riwayat model = riwayatList.get(position);

        holder.textNama.setText("Nama   : " + model.getNama());
        holder.textNIM.setText("NIM    : " + model.getNim());
        holder.textTanggal.setText("Tanggal: " + model.getTanggal());
        holder.textStatus.setText("Status : " + model.getStatus());

        // 🔽 Ubah warna teks status berdasarkan isi
        String status = model.getStatus().toLowerCase().trim();
        switch (status) {
            case "hadir":
                holder.textStatus.setTextColor(0xFF4CAF50); // hijau
                break;
            case "sakit":
            case "izin":
                holder.textStatus.setTextColor(0xFFF44336); // merah
                break;
            default:
                holder.textStatus.setTextColor(0xFF000000); // hitam
                break;
        }
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }
}
