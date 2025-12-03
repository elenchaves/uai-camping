package com.antonia.uaicamping.ui.ajuda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.ui.favoritos.FavoritosActivity;
import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.User;
import com.antonia.uaicamping.ui.anuncio.HomeActivity2;
import com.antonia.uaicamping.ui.cadastroCamping.CadastroCampingActivity;
import com.antonia.uaicamping.ui.perfil.PerfilUsuarioActivity;

public class AjudaActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etMessageBody;
    private Button btnSendMessage;
    private DatabaseHelper dbHelper;
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        dbHelper = new DatabaseHelper(this);
        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        btnBack = findViewById(R.id.btn_back_ajuda);
        etMessageBody = findViewById(R.id.et_message_body);
        btnSendMessage = findViewById(R.id.btn_send_message);

        btnBack.setOnClickListener(v -> finish());
        btnSendMessage.setOnClickListener(v -> sendMessage());


        setupFixedNavigation();
    }

    private void sendMessage() {
        String message = etMessageBody.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "Por favor, escreva sua mensagem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simula o envio por e-mail, tentando obter o e-mail do usuário logado
        String userEmail = "desconhecido@email.com";
        String userName = "Usuário Desconhecido";

        if (currentUserId != -1) {
            User user = dbHelper.getUserById(currentUserId);
            if (user != null) {
                userEmail = user.getEmail();
                userName = user.getName();
            }
        }

        // Simulação de envio para o e-mail de suporte
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"suporte@uaicamping.com.br"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dúvida do Usuário (" + userName + ")");
        intent.putExtra(Intent.EXTRA_TEXT, "E-mail do Usuário: " + userEmail + "\n\n" + message);

        try {
            startActivity(Intent.createChooser(intent, "Enviar mensagem via"));
            etMessageBody.setText("");
            Toast.makeText(this, "Abrindo cliente de e-mail...", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Nenhum aplicativo de e-mail instalado para enviar a mensagem.", Toast.LENGTH_LONG).show();
        }
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
