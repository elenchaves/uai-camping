package com.antonia.uaicamping;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FinalizacaoCadastroActivity extends AppCompatActivity {

    private ImageView ivArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizacao_cadastro);


        ivArrow = findViewById(R.id.imageView4);


        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToHome();
            }
        });
    }

    private void returnToHome() {

        Intent intent = new Intent(FinalizacaoCadastroActivity.this, HomeActivity2.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToHome();
    }
}
