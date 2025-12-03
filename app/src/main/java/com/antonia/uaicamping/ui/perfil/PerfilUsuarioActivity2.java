package com.antonia.uaicamping.ui.perfil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonia.uaicamping.R;
import com.antonia.uaicamping.data.database.DatabaseHelper;
import com.antonia.uaicamping.data.model.User;

import java.util.Calendar;
import java.util.Locale;

public class PerfilUsuarioActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int currentUserId = -1;
    private User currentUser;

    private ImageView imgFoto;
    private EditText etNome, etDataNascimento, etCpf, etTelefone;
    private RadioGroup rgGenero;
    private RadioButton rbFeminino, rbMasculino, rbOutro;
    private Button btnSalvar;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil_usuario2);

        dbHelper = new DatabaseHelper(this);
        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Erro de ID de usuário. Não é possível editar.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        imgFoto = findViewById(R.id.img_profile_picture);
        etNome = findViewById(R.id.et_perfil_nome);
        etDataNascimento = findViewById(R.id.et_perfil_data_nascimento);
        rgGenero = findViewById(R.id.rg_perfil_genero);
        rbFeminino = findViewById(R.id.rb_genero_feminino);
        rbMasculino = findViewById(R.id.rb_genero_masculino);
        rbOutro = findViewById(R.id.rb_genero_outro);
        etCpf = findViewById(R.id.et_perfil_cpf);
        etTelefone = findViewById(R.id.et_perfil_telefone);
        btnSalvar = findViewById(R.id.btn_salvar_perfil);
        btnBack = findViewById(R.id.btn_back_perfil);

        loadUserData();
        etDataNascimento.setOnClickListener(v -> showDatePickerDialog());
        btnBack.setOnClickListener(v -> finish());
        btnSalvar.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        currentUser = dbHelper.getUserById(currentUserId);

        if (currentUser != null) {
            etNome.setText(currentUser.getName());
            etDataNascimento.setText(currentUser.getBirthDate());
            etCpf.setText(currentUser.getCpf());
            etTelefone.setText(currentUser.getPhone());

            String genero = currentUser.getGender();
            if (genero != null) {
                if (genero.equalsIgnoreCase("Feminino")) {
                    rbFeminino.setChecked(true);
                } else if (genero.equalsIgnoreCase("Masculino")) {
                    rbMasculino.setChecked(true);
                } else if (genero.equalsIgnoreCase("Outro")) {
                    rbOutro.setChecked(true);
                }
            }
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            (view, year1, monthOfYear, dayOfMonth) -> {
                String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                etDataNascimento.setText(date);
            }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void saveUserData() {
        if (currentUser == null) return;

        String novoNome = etNome.getText().toString().trim();
        String novaDataNascimento = etDataNascimento.getText().toString().trim();
        String novoCpf = etCpf.getText().toString().trim();
        String novoTelefone = etTelefone.getText().toString().trim();

        String novoGenero = "";
        int selectedId = rgGenero.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            novoGenero = selectedRadioButton.getText().toString();
        }

        if (novoNome.isEmpty() || novaDataNascimento.isEmpty() || novoGenero.isEmpty() || novoCpf.isEmpty() || novoTelefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
            return;
        }

        currentUser.setName(novoNome);
        currentUser.setBirthDate(novaDataNascimento);
        currentUser.setGender(novoGenero);
        currentUser.setCpf(novoCpf);
        currentUser.setPhone(novoTelefone);

        boolean success = dbHelper.updateUser(currentUser);

        if (success) {
            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
