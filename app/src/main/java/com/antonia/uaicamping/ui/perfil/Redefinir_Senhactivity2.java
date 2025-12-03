package com.antonia.uaicamping.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.ui.main.MainActivity;

public class Redefinir_Senhactivity2 extends AppCompatActivity {

    private EditText etEmail, etNewPassword, etConfirmPassword;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senhactivity2);


        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.editTextText);
        etNewPassword = findViewById(R.id.editTextTextEmailAddress3);
        etConfirmPassword = findViewById(R.id.editTextTextPassword);
        btnConfirm = findViewById(R.id.btn_alterar_dados);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "A Nova Senha e a Confirmação de Senha não correspondem.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!dbHelper.checkEmailExists(email)) {
            Toast.makeText(this, "E-mail não encontrado no sistema.", Toast.LENGTH_LONG).show();
            return;
        }

        int rowsAffected = dbHelper.updatePassword(email, newPassword);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Senha redefinida com sucesso! Faça login com sua nova senha.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Redefinir_Senhactivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Falha na redefinição da senha. Tente novamente.", Toast.LENGTH_LONG).show();
        }
    }
}
