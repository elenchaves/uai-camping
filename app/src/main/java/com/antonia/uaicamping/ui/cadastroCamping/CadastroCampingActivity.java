package com.antonia.uaicamping.ui.cadastroCamping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.antonia.uaicamping.ui.anuncio.HomeActivity2;
import com.antonia.uaicamping.ui.main.MainActivity;
import com.antonia.uaicamping.utils.OnFragmentInteractionListener;
import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.Area;

public class CadastroCampingActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Bundle areaDataBundle = new Bundle();
    private int currentStep = 1;
    private int currentUserId = -1;
    private ProgressBar progressBar;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_camping);

        progressBar = findViewById(R.id.progress_bar);
        btnHome = findViewById(R.id.button2);


        progressBar.setMax(4);
        progressBar.setProgress(currentStep);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Erro: Usuário não autenticado. Redirecionando para o login.", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroCampingActivity.this, HomeActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (savedInstanceState == null) {
            showFragment(CadastroCampingFragment1.newInstance());
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );

        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
        progressBar.setProgress(currentStep);
    }

    @Override
    public void onNextStep(Bundle dataBundle) {

        areaDataBundle.putAll(dataBundle);
        currentStep++;

        Fragment nextFragment = null;
        switch (currentStep) {
            case 2:
                nextFragment = CadastroCampingFragment2.newInstance();
                break;
            case 3:
                nextFragment = CadastroCampingFragment3.newInstance();
                break;
            case 4:
                nextFragment = CadastroCampingFragment4.newInstance();
                break;
            default:

                String title = areaDataBundle.getString("TITLE", "Nova Área de Camping");
                String description = areaDataBundle.getString("DESCRIPTION", "Detalhes não preenchidos.");
                String address = areaDataBundle.getString("ADDRESS", "Endereço não definido.");

                double price = areaDataBundle.getDouble("PRICE_PER_NIGHT", 0.0);

                int maxGuests = areaDataBundle.getInt("MAX_GUESTS", 10);
                boolean hasWater = areaDataBundle.getBoolean("HAS_WATER", true);
                boolean hasElectricity = areaDataBundle.getBoolean("HAS_ELECTRICITY", true);


                Area finalArea = new Area(currentUserId, title, description, address, price, maxGuests, hasWater, hasElectricity);

                onFinishCadastro(finalArea);
                return;
        }

        if (nextFragment != null) {
            showFragment(nextFragment);
        }
    }

    @Override
    public void onFinishCadastro(Area finalArea) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        long result = dbHelper.addArea(finalArea);

        if (result > 0) {
            Toast.makeText(this, "Área de Camping cadastrada com sucesso!", Toast.LENGTH_LONG).show();


            Intent intent = new Intent(CadastroCampingActivity.this, FinalizacaoCadastroActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar a área. Tente novamente.", Toast.LENGTH_LONG).show();
        }
    }

    public Bundle getAreaDataBundle() {
        return areaDataBundle;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}
