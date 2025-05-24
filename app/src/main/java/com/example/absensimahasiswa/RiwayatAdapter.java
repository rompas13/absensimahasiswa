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
        TextView textNama, textNIM, textTanggal, textStatus;  // perbaiki deklarasi variabel

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNama    = itemView.findViewById(R.id.textNama);
            textNIM     = itemView.findViewById(R.id.text_view_nim);   // ganti textNIK jadi textNIM
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
        holder.textNIM.setText("NIM    : " + model.getNim());      // ganti getNik() jadi getNim()
        holder.textTanggal.setText("Tanggal: " + model.getTanggal());
        holder.textStatus.setText("Status : " + model.getStatus());
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }
}
