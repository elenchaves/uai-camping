package com.antonia.uaicamping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlanosActivity extends AppCompatActivity {

    private int currentUserId = -1;
    private ImageButton btnBack;
    private Button btnBasico, btnPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planos);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        btnBack = findViewById(R.id.btn_back_planos);
        btnBasico = findViewById(R.id.btn_selecionar_basico);
        btnPremium = findViewById(R.id.btn_selecionar_premium);

        btnBack.setOnClickListener(v -> finish());
        btnBasico.setOnClickListener(v -> selectPlan("Básico"));
        btnPremium.setOnClickListener(v -> selectPlan("Premium"));

        setupFixedNavigation();
    }

    private void selectPlan(String planName) {

        Toast.makeText(this, "Plano " + planName + " selecionado! (Aguardando implementação de pagamento/salvamento)", Toast.LENGTH_LONG).show();
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

        findViewById(R.id.nav_edit).setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroCampingActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilUsuarioActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }
}
