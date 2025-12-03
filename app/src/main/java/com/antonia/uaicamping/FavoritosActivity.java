package com.antonia.uaicamping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView rvFavoritesList;
    private TextView tvEmptyFavorites;
    private DatabaseHelper dbHelper;
    private int currentUserId = -1;


    private ImageButton btnBack, navFavorite, navEdit, navProfile;
    private View navHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        dbHelper = new DatabaseHelper(this);


        rvFavoritesList = findViewById(R.id.rv_favorites_list);
        tvEmptyFavorites = findViewById(R.id.tv_empty_favorites);
        rvFavoritesList.setLayoutManager(new LinearLayoutManager(this));


        btnBack = findViewById(R.id.btn_back_favorites);
        btnBack.setOnClickListener(v -> finish());


        setupFixedNavigation();


        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        if (currentUserId != -1) {
            loadUserFavorites();
        } else {
            Toast.makeText(this, "Usuário não logado. Redirecionando para login.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUserId != -1) {
            loadUserFavorites();
        }
    }

    private void loadUserFavorites() {

        List<Area> favoriteList = dbHelper.getUserFavorites(currentUserId);

        if (favoriteList.isEmpty()) {
            rvFavoritesList.setVisibility(View.GONE);
            tvEmptyFavorites.setVisibility(View.VISIBLE);
        } else {
            rvFavoritesList.setVisibility(View.VISIBLE);
            tvEmptyFavorites.setVisibility(View.GONE);

            AreaAdapter adapter = new AreaAdapter(this, favoriteList, currentUserId);
            rvFavoritesList.setAdapter(adapter);
        }
    }

    private void setupFixedNavigation() {

        navHome = findViewById(R.id.nav_home);
        navFavorite = findViewById(R.id.nav_favorite);
        navEdit = findViewById(R.id.nav_edit);
        navProfile = findViewById(R.id.nav_profile);


        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        navFavorite.setOnClickListener(v -> Toast.makeText(this, "Você já está nos Favoritos.", Toast.LENGTH_SHORT).show());

        navEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroCampingActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilUsuarioActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }
}
