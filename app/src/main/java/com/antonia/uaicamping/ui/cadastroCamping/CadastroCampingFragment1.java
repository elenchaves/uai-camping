package com.antonia.uaicamping.ui.cadastroCamping;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonia.uaicamping.utils.OnFragmentInteractionListener;
import com.antonia.uaicamping.R;

public class CadastroCampingFragment1 extends Fragment {

    private OnFragmentInteractionListener listener;

    private EditText etNome, etCpf, etTelefone, etCep, etEndereco, etNumero, etComplemento, etPontoReferencia, etBairro, etCidade, etEstado;
    private Button btnContinuar;

    public CadastroCampingFragment1() {

    }

    public static CadastroCampingFragment1 newInstance() {
        return new CadastroCampingFragment1();
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

        View view = inflater.inflate(R.layout.fragment_cadastro_camping1, container, false);

        etNome = view.findViewById(R.id.et_nome);
        etCpf = view.findViewById(R.id.et_cpf);
        etTelefone = view.findViewById(R.id.et_telefone);
        etCep = view.findViewById(R.id.et_cep);
        etEndereco = view.findViewById(R.id.et_endereco);
        etNumero = view.findViewById(R.id.et_numero);
        etComplemento = view.findViewById(R.id.et_complemento);
        etPontoReferencia = view.findViewById(R.id.et_ponto_referencia);
        etBairro = view.findViewById(R.id.et_bairro);
        etCidade = view.findViewById(R.id.et_cidade);
        etEstado = view.findViewById(R.id.et_estado);
        btnContinuar = view.findViewById(R.id.btn_continuar);


        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectAndProceed();
            }
        });

        loadExistingData();

        return view;
    }

    private void loadExistingData() {

        Bundle existingData = ((CadastroCampingActivity) requireActivity()).getAreaDataBundle();

        if (existingData.containsKey("CEP")) {
            etNome.setText(existingData.getString("NOME"));
            etCpf.setText(existingData.getString("CPF"));
            etTelefone.setText(existingData.getString("TELEFONE"));
            etCep.setText(existingData.getString("CEP"));
            etEndereco.setText(existingData.getString("ENDERECO"));
            etNumero.setText(existingData.getString("NUMERO"));
            etComplemento.setText(existingData.getString("COMPLEMENTO"));
            etPontoReferencia.setText(existingData.getString("PONTO_REFERENCIA"));
            etBairro.setText(existingData.getString("BAIRRO"));
            etCidade.setText(existingData.getString("CIDADE"));
            etEstado.setText(existingData.getString("ESTADO"));
        }
    }

    private void collectAndProceed() {
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String cep = etCep.getText().toString().trim();
        String endereco = etEndereco.getText().toString().trim();
        String numero = etNumero.getText().toString().trim();
        String complemento = etComplemento.getText().toString().trim();
        String pontoReferencia = etPontoReferencia.getText().toString().trim();
        String bairro = etBairro.getText().toString().trim();
        String cidade = etCidade.getText().toString().trim();
        String estado = etEstado.getText().toString().trim();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || cep.isEmpty() || endereco.isEmpty() || numero.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
            return;
        }

        String fullAddress = String.format("%s, N°%s, %s - %s. CEP: %s. %s, %s. Ref: %s",
            endereco,
            numero,
            bairro,
            complemento.isEmpty() ? "S/C" : complemento,
            cep,
            cidade,
            estado,
            pontoReferencia.isEmpty() ? "N/A" : pontoReferencia);



        Bundle data = new Bundle();

        data.putString("ADDRESS", fullAddress);


        data.putString("NOME", nome);
        data.putString("CPF", cpf);
        data.putString("TELEFONE", telefone);
        data.putString("CEP", cep);
        data.putString("ENDERECO", endereco);
        data.putString("NUMERO", numero);
        data.putString("COMPLEMENTO", complemento);
        data.putString("PONTO_REFERENCIA", pontoReferencia);
        data.putString("BAIRRO", bairro);
        data.putString("CIDADE", cidade);
        data.putString("ESTADO", estado);


        listener.onNextStep(data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
