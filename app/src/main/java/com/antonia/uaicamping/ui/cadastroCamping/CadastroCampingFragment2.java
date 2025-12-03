package com.antonia.uaicamping.ui.cadastroCamping;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonia.uaicamping.utils.OnFragmentInteractionListener;
import com.antonia.uaicamping.R;

public class CadastroCampingFragment2 extends Fragment {

    private static final String TAG = "Fragment2";
    private OnFragmentInteractionListener listener;

    private EditText etNomeCamping, etEmail, etTelefoneCamping, etEnderecoCamping, etBairroCamping, etCidadeCamping, etEstadoCamping, etGpsCoordinates;
    private RadioGroup rgOwnerEmployee;
    private Button btnContinuar, btnVoltar;

    public CadastroCampingFragment2() {

    }

    public static CadastroCampingFragment2 newInstance() {
        return new CadastroCampingFragment2();
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

        View view = inflater.inflate(R.layout.fragment_cadastro_camping2, container, false);

        try {

            etNomeCamping = view.findViewById(R.id.et_nome_camping);
            etEmail = view.findViewById(R.id.et_email);
            etTelefoneCamping = view.findViewById(R.id.et_telefone_camping);
            rgOwnerEmployee = view.findViewById(R.id.rg_owner_employee);
            etEnderecoCamping = view.findViewById(R.id.et_endereco_camping);
            etBairroCamping = view.findViewById(R.id.et_bairro_camping);
            etCidadeCamping = view.findViewById(R.id.et_cidade_camping);
            etEstadoCamping = view.findViewById(R.id.et_estado_camping);
            etGpsCoordinates = view.findViewById(R.id.et_gps_coordinates);
            btnContinuar = view.findViewById(R.id.btn_continuar);
            btnVoltar = view.findViewById(R.id.btn_voltar);
        } catch (Exception e) {
            Log.e(TAG, "ERRO CRÍTICO no mapeamento do Fragment 2. Verifique os IDs no XML.", e);
            Toast.makeText(getContext(), "ERRO: Falha ao carregar Fragment 2. Verifique Logcat.", Toast.LENGTH_LONG).show();
            return null;
        }


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
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, CadastroCampingFragment1.newInstance())
                    .commit();
            }
        });

        loadExistingData();

        return view;
    }

    private void loadExistingData() {
        Bundle existingData = ((CadastroCampingActivity) requireActivity()).getAreaDataBundle();

        if (existingData.containsKey("CAMPING_NAME")) {
            etNomeCamping.setText(existingData.getString("CAMPING_NAME"));
            etEmail.setText(existingData.getString("CAMPING_EMAIL"));
            etTelefoneCamping.setText(existingData.getString("CAMPING_PHONE"));
            etEnderecoCamping.setText(existingData.getString("CAMPING_ADDRESS_STREET"));
            etBairroCamping.setText(existingData.getString("CAMPING_ADDRESS_NEIGHBORHOOD"));
            etCidadeCamping.setText(existingData.getString("CAMPING_ADDRESS_CITY"));
            etEstadoCamping.setText(existingData.getString("CAMPING_ADDRESS_STATE"));
            etGpsCoordinates.setText(existingData.getString("GPS_COORDINATES"));

            String ownerStatus = existingData.getString("OWNER_STATUS", "");
            if ("EMPLOYEE".equals(ownerStatus)) {

                rgOwnerEmployee.check(R.id.rb_employee);
            } else {
                rgOwnerEmployee.check(R.id.rb_owner);
            }
        }
    }

    private void collectAndProceed() {
        String nomeCamping = etNomeCamping.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefoneCamping = etTelefoneCamping.getText().toString().trim();
        String enderecoCamping = etEnderecoCamping.getText().toString().trim();
        String bairroCamping = etBairroCamping.getText().toString().trim();
        String cidadeCamping = etCidadeCamping.getText().toString().trim();
        String estadoCamping = etEstadoCamping.getText().toString().trim();
        String gpsCoordinates = etGpsCoordinates.getText().toString().trim();

        int selectedOwnerStatusId = rgOwnerEmployee.getCheckedRadioButtonId();
        String ownerStatus = "";
        if (selectedOwnerStatusId == R.id.rb_owner) {
            ownerStatus = "OWNER";
        } else if (selectedOwnerStatusId == R.id.rb_employee) {
            ownerStatus = "EMPLOYEE";
        }

        if (nomeCamping.isEmpty() || email.isEmpty() || telefoneCamping.isEmpty() || enderecoCamping.isEmpty() || bairroCamping.isEmpty() || cidadeCamping.isEmpty() || estadoCamping.isEmpty() || selectedOwnerStatusId == -1) {
            Toast.makeText(getContext(), "Por favor, preencha todos os dados de contato, endereço e status.", Toast.LENGTH_LONG).show();
            return;
        }

        String fullAddress = String.format("%s, %s, %s/%s. Coordenadas: %s",
            enderecoCamping,
            bairroCamping,
            cidadeCamping,
            estadoCamping,
            gpsCoordinates.isEmpty() ? "N/A" : gpsCoordinates);

        Bundle data = new Bundle();

        data.putString("TITLE", nomeCamping);
        data.putString("ADDRESS", fullAddress);

        data.putString("CAMPING_EMAIL", email);
        data.putString("CAMPING_PHONE", telefoneCamping);
        data.putString("OWNER_STATUS", ownerStatus);
        data.putString("GPS_COORDINATES", gpsCoordinates);

        data.putString("CAMPING_NAME", nomeCamping);
        data.putString("CAMPING_ADDRESS_STREET", enderecoCamping);
        data.putString("CAMPING_ADDRESS_NEIGHBORHOOD", bairroCamping);
        data.putString("CAMPING_ADDRESS_CITY", cidadeCamping);
        data.putString("CAMPING_ADDRESS_STATE", estadoCamping);

        listener.onNextStep(data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
