package com.antonia.uaicamping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CadastroCampingFragment3 extends Fragment {

    private OnFragmentInteractionListener listener;

    private EditText etPricePerNight, etSite, etFacebook, etInstagram, etDescricaoCamping;
    private Button btnAdicionarFotos, btnVoltar, btnContinuar;

    private ActivityResultLauncher<String> mGetContent;

    public CadastroCampingFragment3() {

    }

    public static CadastroCampingFragment3 newInstance() {
        return new CadastroCampingFragment3();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    Toast.makeText(getContext(), "Foto selecionada! URI: " + uri.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Nenhuma foto selecionada.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_camping3, container, false);

        etPricePerNight = view.findViewById(R.id.et_price_per_night); // NOVO MAPPEAMENTO
        etSite = view.findViewById(R.id.et_site_camping);
        etFacebook = view.findViewById(R.id.et_facebook);
        etInstagram = view.findViewById(R.id.et_instagram);
        etDescricaoCamping = view.findViewById(R.id.et_descricao_camping);
        btnAdicionarFotos = view.findViewById(R.id.btn_adicionar_fotos);
        btnVoltar = view.findViewById(R.id.btn_voltar_3);
        btnContinuar = view.findViewById(R.id.btn_continuar_3);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectAndProceed();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CadastroCampingFragment2.newInstance())
                    .commit();
            }
        });


        btnAdicionarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        loadExistingData();

        return view;
    }

    private void loadExistingData() {
        Bundle existingData = ((CadastroCampingActivity) requireActivity()).getAreaDataBundle();

        if (existingData.containsKey("DESCRIPTION")) {
            if (existingData.containsKey("PRICE_PER_NIGHT")) {
                etPricePerNight.setText(String.valueOf(existingData.getDouble("PRICE_PER_NIGHT")));
            }

            etDescricaoCamping.setText(existingData.getString("DESCRIPTION"));
            etSite.setText(existingData.getString("SITE_URL"));
            etFacebook.setText(existingData.getString("FACEBOOK_URL"));
            etInstagram.setText(existingData.getString("INSTAGRAM_URL"));
        }
    }

    private void collectAndProceed() {
        String precoString = etPricePerNight.getText().toString().trim();
        String descricao = etDescricaoCamping.getText().toString().trim();
        String site = etSite.getText().toString().trim();
        String facebook = etFacebook.getText().toString().trim();
        String instagram = etInstagram.getText().toString().trim();

        if (descricao.isEmpty()) {
            Toast.makeText(getContext(), "A Descrição do Camping é obrigatória.", Toast.LENGTH_LONG).show();
            return;
        }
        if (precoString.isEmpty()) {
            Toast.makeText(getContext(), "O preço da diária é obrigatório.", Toast.LENGTH_LONG).show();
            return;
        }

        double pricePerNight;
        try {
            pricePerNight = Double.parseDouble(precoString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Formato de preço inválido. Use apenas números (ex: 50.00).", Toast.LENGTH_LONG).show();
            return;
        }

        Bundle data = new Bundle();

        data.putDouble("PRICE_PER_NIGHT", pricePerNight);

        data.putString("DESCRIPTION", descricao);
        data.putString("SITE_URL", site);
        data.putString("FACEBOOK_URL", facebook);
        data.putString("INSTAGRAM_URL", instagram);

        listener.onNextStep(data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
