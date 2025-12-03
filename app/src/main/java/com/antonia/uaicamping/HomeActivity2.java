package com.antonia.uaicamping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity2 extends AppCompatActivity {

    private RecyclerView rvCampingList;
    private DatabaseHelper dbHelper;
    private int currentUserId = -1;
    private String currentUserEmail = "";

    private EditText etSearchBar;
    private Button btnHomeNav;
    private ImageButton btnAddListingNav, btnProfileNav, btnFavoritesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        currentUserId = intent.getIntExtra("USER_ID", -1);
        currentUserEmail = intent.getStringExtra("USER_EMAIL");

        etSearchBar = findViewById(R.id.et_search_bar);
        rvCampingList = findViewById(R.id.rv_camping_list_main);
        rvCampingList.setLayoutManager(new LinearLayoutManager(this));

        btnHomeNav = findViewById(R.id.materialButton2);
        btnFavoritesNav = findViewById(R.id.btn_favorito);
        btnAddListingNav = findViewById(R.id.imageButton);
        btnProfileNav = findViewById(R.id.btn_perfil);

        loadCampingAreas();

        btnHomeNav.setOnClickListener(v -> Toast.makeText(this, "Você já está na tela principal.", Toast.LENGTH_SHORT).show());

        btnAddListingNav.setOnClickListener(v -> {
            if (currentUserId != -1) {
                Intent cadastroIntent = new Intent(HomeActivity2.this, CadastroCampingActivity.class);
                cadastroIntent.putExtra("USER_ID", currentUserId);
                startActivity(cadastroIntent);
            } else {
                Toast.makeText(this, "Faça login para adicionar um anúncio.", Toast.LENGTH_SHORT).show();
            }
        });


        btnProfileNav.setOnClickListener(v -> {
            if (currentUserId != -1) {
                Intent profileIntent = new Intent(HomeActivity2.this, PerfilUsuarioActivity.class);
                profileIntent.putExtra("USER_ID", currentUserId);
                startActivity(profileIntent);
            } else {
                Toast.makeText(this, "Faça login para ver seu perfil.", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavoritesNav.setOnClickListener(v -> {
            if (currentUserId != -1) {
                Intent favoritesIntent = new Intent(HomeActivity2.this, FavoritosActivity.class);
                favoritesIntent.putExtra("USER_ID", currentUserId);
                startActivity(favoritesIntent);
            } else {
                Toast.makeText(this, "Faça login para acessar favoritos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCampingAreas() {
        // 1. Obtém todos os dados do banco de dados
        List<Area> areaList = dbHelper.getAllAreas();

        if (areaList.isEmpty()) {
            Toast.makeText(this, "Nenhuma área de camping cadastrada. Use o ícone de adição para cadastrar.", Toast.LENGTH_LONG).show();
        }

        AreaAdapter adapter = new AreaAdapter(this, areaList, currentUserId);
        rvCampingList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCampingAreas();
    }
}
