package com.antonia.uaicamping.ui.anuncio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.utils.SessionManager;
import com.antonia.uaicamping.adapters.AreaAdapter;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.ui.cadastroCamping.CadastroCampingActivity;
import com.antonia.uaicamping.ui.favoritos.FavoritosActivity;
import com.antonia.uaicamping.ui.main.MainActivity;
import com.antonia.uaicamping.ui.perfil.PerfilUsuarioActivity;
import com.antonia.uaicamping.data.model.Area;


//import org.checkerframework.checker.units.qual.Area;

import java.util.List;

public class HomeActivity2 extends AppCompatActivity {

    private RecyclerView rvCampingList;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
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
        sessionManager = new SessionManager(this);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            currentUserId = sessionManager.getUserId();
        }


        if (currentUserId == -1) {

            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }


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

            Intent cadastroIntent = new Intent(HomeActivity2.this, CadastroCampingActivity.class);
            cadastroIntent.putExtra("USER_ID", currentUserId);
            startActivity(cadastroIntent);
        });

        btnProfileNav.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HomeActivity2.this, PerfilUsuarioActivity.class);
            profileIntent.putExtra("USER_ID", currentUserId);
            startActivity(profileIntent);
        });


        btnFavoritesNav.setOnClickListener(v -> {
            Intent favoritesIntent = new Intent(HomeActivity2.this, FavoritosActivity.class);
            favoritesIntent.putExtra("USER_ID", currentUserId);
            startActivity(favoritesIntent);
        });
    }

    private void loadCampingAreas() {

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
