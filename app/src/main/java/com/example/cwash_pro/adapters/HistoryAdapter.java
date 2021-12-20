package com.example.cwash_pro.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cwash_pro.R;
import com.example.cwash_pro.myinterface.ItemClick;
import com.example.cwash_pro.models.Schedule;
import com.github.nikartm.button.FitButton;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<Schedule> schedules;
    ItemClick itemClick;

    public HistoryAdapter(Context context, List<Schedule> schedules, ItemClick itemClick) {
        this.context = context;
        this.schedules = schedules;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = schedules.get(position).getTimeBook();
        String hour = time.substring(0, 6);
        String date = time.substring(7);
        holder.tvTime.setText( hour + " - " + date);
        String service = "";
        for (int i = 0; i < schedules.get(position).getServices().size(); i++) {
            service = schedules.get(position).getServices().get(i).getName();
        }
        service = service + "";
        holder.tvStatus.setTextColor(Color.GREEN);
        holder.img.setImageResource(R.drawable.ic_done);
        holder.tvVehicle.setText(schedules.get(position).getVehicle().getBrand());
        if (schedules.get(position).getStatus().equals("Confirmed") && !schedules.get(position).getVehicleStatus()) {
            holder.btnConfirmVehicle.setVisibility(View.VISIBLE);
            holder.img.setImageResource(R.drawable.ic_done);
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.tvStatus.setText("Đã xác nhận");
        }
        if (schedules.get(position).getStatus().equals("Pending")) {
            holder.btnCancelSchedule.setVisibility(View.VISIBLE);
            holder.img.setImageResource(R.drawable.ic_pending);
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvStatus.setText("Đang chờ xác nhận");
        }
        if (schedules.get(position).getStatus().equals("Cancelled")) {
            holder.tvStatus.setTextColor(Color.BLUE);
            holder.img.setImageResource(R.drawable.ic_cancel);
            holder.tvStatus.setText("Đã huỷ");
        }
        holder.tvService.setText(service);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTime, tvVehicle, tvStatus, tvService;
        FitButton btnConfirmVehicle, btnCancelSchedule;
        LinearLayout layout_bottom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layout_bottom = itemView.findViewById(R.id.layout_bottom);
            img = itemView.findViewById(R.id.img);
            tvService = itemView.findViewById(R.id.tvService);
            btnConfirmVehicle = itemView.findViewById(R.id.btnConfirmVehicle);
            btnCancelSchedule = itemView.findViewById(R.id.btnCancelSchedule);
            btnCancelSchedule.setOnClickListener(v -> itemClick.setOnItemClick(v, getAdapterPosition()));
            btnConfirmVehicle.setOnClickListener(v -> itemClick.setOnItemClick(v, getAdapterPosition()));
        }
    }
}
