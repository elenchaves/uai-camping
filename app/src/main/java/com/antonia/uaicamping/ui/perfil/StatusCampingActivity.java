package com.antonia.uaicamping.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.Area;
import com.antonia.uaicamping.ui.anuncio.HomeActivity2;
import com.antonia.uaicamping.ui.cadastroCamping.CadastroCampingActivity;
import com.antonia.uaicamping.ui.favoritos.FavoritosActivity;
import com.antonia.uaicamping.ui.planos.PlanosActivity;

import java.util.List;

public class StatusCampingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int currentUserId = -1;

    private TextView tvAreaName, tvSentDate, tvCurrentStatus;
    private Button btnMudarPlano;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_camping);

        dbHelper = new DatabaseHelper(this);
        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        tvAreaName = findViewById(R.id.tv_area_name);
        tvSentDate = findViewById(R.id.tv_sent_date);
        tvCurrentStatus = findViewById(R.id.tv_current_status);
        btnMudarPlano = findViewById(R.id.btn_mudar_plano_status);
        btnBack = findViewById(R.id.btn_back_status);

        btnBack.setOnClickListener(v -> finish());
        btnMudarPlano.setOnClickListener(v -> navigateToPlanos());

        if (currentUserId != -1) {
            loadLastCampingStatus();
        } else {
            Toast.makeText(this, "Usuário não autenticado. Redirecionando.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupFixedNavigation();
    }

    private void loadLastCampingStatus() {
        List<Area> userAreas = dbHelper.getUserAreas(currentUserId);

        if (userAreas == null || userAreas.isEmpty()) {
            tvAreaName.setText("Nenhum cadastro encontrado.");
            tvSentDate.setText("");
            tvCurrentStatus.setText("Incompleto");
            btnMudarPlano.setVisibility(View.GONE);
            return;
        }

        Area lastArea = userAreas.get(0);

        tvAreaName.setText("Camping: " + lastArea.getTitle());
        tvSentDate.setText("Enviado em: 25/10/2025");
        tvCurrentStatus.setText("Em Análise");

        btnMudarPlano.setVisibility(View.VISIBLE);
    }

    private void navigateToPlanos() {
        Intent intent = new Intent(this, PlanosActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    // Configuração do Menu de Navegação Inferior
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
