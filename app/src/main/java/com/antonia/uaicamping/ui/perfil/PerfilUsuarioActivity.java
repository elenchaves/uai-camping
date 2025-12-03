package com.antonia.uaicamping.ui.perfil;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.utils.SessionManager;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.User;
import com.antonia.uaicamping.ui.ajuda.AjudaActivity;
import com.antonia.uaicamping.ui.anuncio.HomeActivity2;
import com.antonia.uaicamping.ui.cadastroCamping.CadastroCampingActivity;
import com.antonia.uaicamping.ui.favoritos.FavoritosActivity;
import com.antonia.uaicamping.ui.main.MainActivity;
import com.google.android.material.navigation.NavigationView;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private int currentUserId = -1;
    private User currentUser;

    private TextView tvNome, tvEmail, tvDataNascimento, tvGenero, tvCpf, tvTelefone;
    private ImageButton btnMenuDrawer;
    private Button btnAlterarDados;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    private ActivityResultLauncher<Intent> editProfileLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);


        editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK) {

                    loadUserProfile(currentUserId);
                    Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
                }
            });


        tvNome = findViewById(R.id.tv_nome);
        tvEmail = findViewById(R.id.tv_email_perfil);
        tvDataNascimento = findViewById(R.id.tv_data_nascimento);
        tvGenero = findViewById(R.id.tv_genero);
        tvCpf = findViewById(R.id.tv_cpf);
        tvTelefone = findViewById(R.id.tv_telefone);

        btnMenuDrawer = findViewById(R.id.btn_menu);
        btnAlterarDados = findViewById(R.id.btn_alterar_dados);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            currentUserId = sessionManager.getUserId();
        }

        if (currentUserId != -1) {
            loadUserProfile(currentUserId);
        } else {

            Toast.makeText(this, "Sessão expirada. Redirecionando para o login.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        setupDrawerNavigation();
        setupFixedNavigation();

        btnAlterarDados.setOnClickListener(v -> navigateToEditProfile());
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, PerfilUsuarioActivity2.class);
        intent.putExtra("USER_ID", currentUserId);
        editProfileLauncher.launch(intent);
    }

    private void loadUserProfile(int userId) {
        currentUser = dbHelper.getUserById(userId);

        if (currentUser != null) {

            tvNome.setText(currentUser.getName());
            tvEmail.setText(currentUser.getEmail());
            tvDataNascimento.setText(currentUser.getBirthDate());
            tvGenero.setText(currentUser.getGender());
            tvCpf.setText(currentUser.getCpf());
            tvTelefone.setText(currentUser.getPhone());


            View headerView = navigationView.getHeaderView(0);
            TextView tvHeaderEmail = headerView.findViewById(R.id.tv_header_email);
            if (tvHeaderEmail != null) {
                tvHeaderEmail.setText(currentUser.getEmail());
            }

        } else {
            Toast.makeText(this, "Dados do usuário não encontrados.", Toast.LENGTH_LONG).show();
        }
    }

    private void setupDrawerNavigation() {

        btnMenuDrawer.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_cadastro_camping) {
                navigateToCadastroCamping();
            } else if (id == R.id.nav_status) {

                Intent intent = new Intent(PerfilUsuarioActivity.this, StatusCampingActivity.class);
                intent.putExtra("USER_ID", currentUserId);
                startActivity(intent);
            } else if (id == R.id.nav_ajuda) {

                Intent intent = new Intent(PerfilUsuarioActivity.this, AjudaActivity.class);
                intent.putExtra("USER_ID", currentUserId);
                startActivity(intent);
            } else if (id == R.id.nav_sair) {

                sessionManager.logout();
                Toast.makeText(PerfilUsuarioActivity.this, "Logout realizado.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PerfilUsuarioActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupFixedNavigation() {



        findViewById(R.id.nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        findViewById(R.id.nav_favorite).setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritosActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });


        findViewById(R.id.nav_edit).setOnClickListener(v -> navigateToCadastroCamping());


        findViewById(R.id.nav_profile).setOnClickListener(v -> Toast.makeText(this, "Você já está no Perfil.", Toast.LENGTH_SHORT).show());
    }

    private void navigateToCadastroCamping() {
        Intent intent = new Intent(this, CadastroCampingActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
