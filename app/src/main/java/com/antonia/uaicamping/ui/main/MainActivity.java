package com.antonia.uaicamping.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.utils.SessionManager;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.ui.anuncio.HomeActivity2;
import com.antonia.uaicamping.ui.perfil.CadastrarActivity;
import com.antonia.uaicamping.ui.perfil.Redefinir_Senhactivity2;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink, tvForgotPassword;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);


        int savedUserId = sessionManager.getUserId();
        if (savedUserId != -1) {
            redirectToHome(savedUserId);
            return;
        }


        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPassword = findViewById(R.id.editTextTextEmailAddress2);
        btnLogin = findViewById(R.id.btn_alterar_dados);
        tvRegisterLink = findViewById(R.id.textView2);
        tvForgotPassword = findViewById(R.id.textView);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastrarActivity.class);
                startActivity(intent);
            }
        });


        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Redefinir_Senhactivity2.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o e-mail e a senha.", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isValid = dbHelper.checkUser(email, password);

        if (isValid) {

            int userId = dbHelper.getUserIdByEmail(email);

            sessionManager.saveUserId(userId);

            redirectToHome(userId);

        } else {
            Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
        }
    }


    private void redirectToHome(int userId) {
        Intent intent = new Intent(MainActivity.this, HomeActivity2.class);
        intent.putExtra("USER_ID", userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
