package com.antonia.uaicamping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private final List<Area> areaList;
    private final Context context;
    private final int currentUserId;


    public AreaAdapter(Context context, List<Area> areaList, int currentUserId) {
        this.context = context;
        this.areaList = areaList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camping_card, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);

        holder.tvTitle.setText(area.getTitle());

        String[] addressParts = area.getAddress().split(",\\s*");
        String cityState = (addressParts.length > 0) ? addressParts[addressParts.length - 1] : area.getAddress();
        holder.tvAddress.setText(cityState);

        String priceText = String.format(Locale.getDefault(), "R$ %.2f / noite", area.getPricePerNight());
        holder.tvPrice.setText(priceText);

        holder.tvRating.setText("4,5");

        // LÓGICA DO CLIQUE DO CARD
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnuncioActivity.class);
            intent.putExtra("AREA_ID", area.getId());
            intent.putExtra("USER_ID", currentUserId);
            context.startActivity(intent);
        });

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        if (currentUserId > 0 && dbHelper.isAreaFavorite(currentUserId, area.getId())) {
            holder.ivFavoriteIcon.setImageResource(R.drawable.coracao_24);
        } else {
            holder.ivFavoriteIcon.setImageResource(R.drawable.coracao);
        }

        holder.ivFavoriteIcon.setOnClickListener(v -> {
            if (context instanceof HomeActivity2) {
                Toast.makeText(context, "Clique no anúncio para favoritar/desfavoritar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddress, tvPrice, tvRating;
        ImageView ivImage, ivFavoriteIcon;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_area_title);
            tvAddress = itemView.findViewById(R.id.tv_area_address);
            tvPrice = itemView.findViewById(R.id.tv_area_price);
            tvRating = itemView.findViewById(R.id.tv_area_rating);

            ivImage = itemView.findViewById(R.id.iv_area_image);
            ivFavoriteIcon = itemView.findViewById(R.id.iv_favorite_icon);
        }
    }
}
