package com.antonia.uaicamping.ui.anuncio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.ui.favoritos.FavoritosActivity;
import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.Area;
import com.antonia.uaicamping.data.model.User;
import com.antonia.uaicamping.ui.cadastroCamping.CadastroCampingActivity;
import com.antonia.uaicamping.ui.perfil.PerfilUsuarioActivity;

import java.util.Locale;

public class AnuncioActivity extends AppCompatActivity {

    private static final String TAG = "AnuncioActivity";
    private TextView txtTitulo, txtOQueEspera, txtRegrasConvivencia, txtPreco, txtAnfitriaNome;
    private ImageButton btnBack, btnFavorite, btnWhatsapp;

    private View navHome;
    private ImageButton navFavorite, navAdd, navProfile;

    private DatabaseHelper dbHelper;
    private Area currentArea;
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_anuncio);
            dbHelper = new DatabaseHelper(this);

            txtTitulo = findViewById(R.id.txt_titulo);
            txtOQueEspera = findViewById(R.id.txt_o_que_espera);
            txtRegrasConvivencia = findViewById(R.id.txt_regras_convivencia);
            txtPreco = findViewById(R.id.txt_preco);
            txtAnfitriaNome = findViewById(R.id.txt_anfitria_nome);


            btnBack = findViewById(R.id.arrow_back);
            btnFavorite = findViewById(R.id.btn_favorite);
            btnWhatsapp = findViewById(R.id.ic_whatsapp);

            if (btnBack == null || btnFavorite == null || btnWhatsapp == null) {
                throw new NullPointerException("Um ou mais botões essenciais não foram encontrados no layout. Verifique os IDs.");
            }

            int areaId = getIntent().getIntExtra("AREA_ID", -1);
            currentUserId = getIntent().getIntExtra("USER_ID", -1);

            Log.d(TAG, "ID do Usuário Recebido: " + currentUserId);

            if (areaId != -1) {
                loadAreaDetails(areaId);
            } else {
                Toast.makeText(this, "Erro: ID do anúncio não fornecido.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            btnBack.setOnClickListener(v -> finish());
            btnWhatsapp.setOnClickListener(v -> sendWhatsappMessage());
            btnFavorite.setOnClickListener(v -> toggleFavorite());

            setupFixedNavigation();

        } catch (Exception e) {
            Log.e(TAG, "ERRO CRÍTICO na inicialização da AnuncioActivity.", e);
            Toast.makeText(this, "ERRO: Falha ao carregar detalhes do anúncio.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadAreaDetails(int areaId) {
        currentArea = dbHelper.getAreaById(areaId);

        if (currentArea != null) {

            txtTitulo.setText(currentArea.getTitle());
            txtPreco.setText(String.format(Locale.getDefault(), "R$ %.2f por pessoa / diária", currentArea.getPricePerNight()));


            String comodidades = (currentArea.isHasWater() ? "- Água no local\n" : "") +
                (currentArea.isHasElectricity() ? "- Eletricidade disponível\n" : "") +
                "- Máximo de hóspedes: " + currentArea.getMaxGuests();
            txtOQueEspera.setText(comodidades + "\nEndereço: " + currentArea.getAddress());

            txtRegrasConvivencia.setText(currentArea.getDescription());

            // Carregar Detalhes do Anfitrião
            User host = dbHelper.getUserById(currentArea.getUserId());
            if (host != null) {
                txtAnfitriaNome.setText("Anfitriã: " + host.getName());
            } else {
                txtAnfitriaNome.setText("Anfitriã: (Desconhecida)");
            }

            updateFavoriteButtonState();

        } else {
            Toast.makeText(this, "Anúncio não encontrado no banco de dados.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void updateFavoriteButtonState() {

        if (currentArea != null && currentUserId > 0) {
            boolean isFavorite = dbHelper.isAreaFavorite(currentUserId, currentArea.getId());

            if (isFavorite) {
                btnFavorite.setImageResource(R.drawable.coracao_24);
                btnFavorite.setContentDescription("Desfavoritar");
            } else {
                btnFavorite.setImageResource(R.drawable.coracao);
                btnFavorite.setContentDescription("Favoritar");
            }
            btnFavorite.setEnabled(true);
        } else {
            btnFavorite.setEnabled(false);
        }
    }

    private void toggleFavorite() {
        if (currentArea == null || currentUserId <= 0) {
            Toast.makeText(this, "Erro: Faça login para favoritar/desfavoritar.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCurrentlyFavorite = dbHelper.isAreaFavorite(currentUserId, currentArea.getId());
        boolean success = dbHelper.toggleFavorite(currentUserId, currentArea.getId(), !isCurrentlyFavorite);

        if (success) {
            String message = isCurrentlyFavorite ? "Removido dos favoritos." : "Adicionado aos favoritos!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            updateFavoriteButtonState();
        } else {
            Toast.makeText(this, "Falha ao atualizar favoritos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFixedNavigation() {

        navHome = findViewById(R.id.btn_nav_home);
        navFavorite = findViewById(R.id.btn_nav_favorite);
        navAdd = findViewById(R.id.btn_nav_add);
        navProfile = findViewById(R.id.btn_nav_profile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        navFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritosActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

        navAdd.setOnClickListener(v -> navigateToCadastroCamping());

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilUsuarioActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }

    private void navigateToCadastroCamping() {
        Intent intent = new Intent(this, CadastroCampingActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    private void sendWhatsappMessage() {
        if (currentArea == null) return;
        String phoneNumber = "5531999999999";
        String message = String.format("Olá! Vi o anúncio do camping '%s' no Uai Camping e gostaria de saber sobre reservas.", currentArea.getTitle());
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumber, message);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir o WhatsApp.", Toast.LENGTH_SHORT).show();
        }
    }
}
