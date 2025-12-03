package com.antonia.uaicamping.ui.cadastroCamping;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonia.uaicamping.utils.OnFragmentInteractionListener;
import com.antonia.uaicamping.R;

public class CadastroCampingFragment4 extends Fragment {

    private OnFragmentInteractionListener listener;


    private RadioGroup rgBarracas, rgRvs, rgChales, rgAnimais;
    private Button btnVoltar, btnFinalizar;

    private static final String KEY_PRICE_PER_NIGHT = "PRICE_PER_NIGHT";
    private static final String KEY_MAX_GUESTS = "MAX_GUESTS";
    private static final String KEY_HAS_WATER = "HAS_WATER";
    private static final String KEY_HAS_ELECTRICITY = "HAS_ELECTRICITY";

    public CadastroCampingFragment4() {

    }

    public static CadastroCampingFragment4 newInstance() {
        return new CadastroCampingFragment4();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " deve implementar OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_camping4, container, false);

        rgBarracas = view.findViewById(R.id.rg_barracas);
        rgRvs = view.findViewById(R.id.rg_rvs);
        rgChales = view.findViewById(R.id.rg_chales);
        rgAnimais = view.findViewById(R.id.rg_animais);
        btnVoltar = view.findViewById(R.id.btn_voltar_4);
        btnFinalizar = view.findViewById(R.id.btn_finalizar);

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectAndFinalize();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CadastroCampingFragment3.newInstance())
                    .commit();
            }
        });

        loadExistingData();

        return view;
    }

    private void loadExistingData() {

        Bundle existingData = ((CadastroCampingActivity) requireActivity()).getAreaDataBundle();

        if (existingData.containsKey("ACEITA_BARRACAS")) {
            boolean aceitaBarracas = existingData.getBoolean("ACEITA_BARRACAS");
            if (aceitaBarracas) {
                rgBarracas.check(R.id.rb_aceita_barracas);
            } else {
                rgBarracas.check(R.id.rb_nao_aceita_barracas);
            }
        }

    }

    private void collectAndFinalize() {

        boolean aceitaBarracas = rgBarracas.getCheckedRadioButtonId() == R.id.rb_aceita_barracas;
        boolean aceitaRvs = rgRvs.getCheckedRadioButtonId() == R.id.rb_aceita_rvs;
        boolean possuiChales = rgChales.getCheckedRadioButtonId() == R.id.rb_possui_chales;

        int petStatusId = rgAnimais.getCheckedRadioButtonId();
        String petStatus;
        if (petStatusId == R.id.rb_aceita_animais) {
            petStatus = "Aceita animais sem restrições.";
        } else if (petStatusId == R.id.rb_nao_aceita_animais) {
            petStatus = "Não aceita animais.";
        } else {
            petStatus = "Aceita animais com restrições.";
        }

        Bundle data = new Bundle();
        Bundle existingData = ((CadastroCampingActivity) requireActivity()).getAreaDataBundle();
        data.putAll(existingData);

        data.putBoolean("ACEITA_BARRACAS", aceitaBarracas);
        data.putBoolean("ACEITA_RVS", aceitaRvs);
        data.putBoolean("POSSUI_CHALES", possuiChales);
        data.putString("PET_STATUS", petStatus);

        listener.onNextStep(data);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
