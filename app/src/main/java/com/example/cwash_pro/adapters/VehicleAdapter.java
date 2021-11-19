package com.example.cwash_pro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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
        holder.tvNameOfVehicle.setText(vehicles.get(position).getName());
        holder.tvLicense.setText(vehicles.get(position).getLicense());
        Log.e("onBindViewHolder: ", vehicles.get(position).getType());
        if (vehicles.get(position).getType().equals("Car")) {
            Log.e("onBindViewHolder1: ", vehicles.get(position).getType());
            holder.lottieAnimationView.setAnimation("car-animation.json");
            holder.lottieAnimationView.playAnimation();
           // holder.imgVehicle.setImageResource(R.drawable.carcar);
        } else if (vehicles.get(position).getType().equals("Motorcycle")) {
            Log.e("onBindViewHolder2: ", vehicles.get(position).getType());
            holder.lottieAnimationView.setAnimation("motorcycle-animation.json");
            holder.lottieAnimationView.playAnimation();        }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class VehicleHolder extends RecyclerView.ViewHolder {
        ImageView  imgDelete, imgUpdate;
        TextView tvNameOfVehicle, tvLicense;
        LottieAnimationView lottieAnimationView;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);
            lottieAnimationView = itemView.findViewById(R.id.imgVehicle);
            tvNameOfVehicle = (TextView) itemView.findViewById(R.id.tvNameOfVehicle);
            tvLicense = (TextView) itemView.findViewById(R.id.tvLicense);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgUpdate = (ImageView) itemView.findViewById(R.id.imgUpdate);
            imgUpdate.setOnClickListener(v -> {
                itemClick.setOnItemClick(v, getAdapterPosition());
            });
            imgDelete.setOnClickListener(v -> {
                itemClick.setOnItemClick(v, getAdapterPosition());
            });
        }
    }
}

