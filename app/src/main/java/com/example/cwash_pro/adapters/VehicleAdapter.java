package com.example.cwash_pro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cwash_pro.R;
import com.example.cwash_pro.myinterface.ItemClick;
import com.example.cwash_pro.models.Vehicle;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {
    private Context context;
    private List<Vehicle> vehicles;
    private ItemClick itemClick;

    public VehicleAdapter(Context context, List<Vehicle> vehicles, ItemClick itemClick) {
        this.context = context;
        this.vehicles = vehicles;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public VehicleAdapter.VehicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vehicles, parent, false);
        return new VehicleAdapter.VehicleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHolder holder, int position) {
        holder.tvNameOfVehicle.setText(vehicles.get(position).getBrand());
        holder.tvLicense.setText(vehicles.get(position).getLicense());
        if (vehicles.get(position).getType().equals("Car")) {
             holder.imgVehicle.setImageResource(R.drawable.ic_car);
        } else if (vehicles.get(position).getType().equals("Motorcycle")) {
             holder.imgVehicle.setImageResource(R.drawable.ic_motorcycle);

        }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class VehicleHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete, imgUpdate, imgVehicle;
        TextView tvNameOfVehicle, tvLicense;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            tvNameOfVehicle = itemView.findViewById(R.id.tvNameOfVehicle);
            tvLicense =  itemView.findViewById(R.id.tvLicense);
            imgDelete =  itemView.findViewById(R.id.imgDelete);
            imgUpdate =  itemView.findViewById(R.id.imgUpdate);
            imgUpdate.setOnClickListener(v -> {
                itemClick.setOnItemClick(v, getAdapterPosition());
            });
            imgDelete.setOnClickListener(v -> {
                itemClick.setOnItemClick(v, getAdapterPosition());
            });
        }
    }
}

