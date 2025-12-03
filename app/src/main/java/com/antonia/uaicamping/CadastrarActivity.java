package com.antonia.uaicamping;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class CadastrarActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etCpf, etPhone, etBirthDate;
    private RadioGroup rgGender;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        dbHelper = new DatabaseHelper(this);


        etName = findViewById(R.id.et_name);
        etCpf = findViewById(R.id.et_cpf);
        etPhone = findViewById(R.id.et_phone);
        etBirthDate = findViewById(R.id.et_birth_date);
        rgGender = findViewById(R.id.rg_gender);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);

        // Configurar listener para o campo Data de Nascimento (abre o seletor)
        etBirthDate.setOnClickListener(v -> showDatePickerDialog());
        etBirthDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePickerDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            (view, year1, monthOfYear, dayOfMonth) -> {

                String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                etBirthDate.setText(date);
            }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        int selectedId = rgGender.getCheckedRadioButtonId();
        String gender = "";

        if (selectedId != -1) {

            RadioButton rb = findViewById(selectedId);
            gender = rb.getText().toString();
        }


        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpf.isEmpty() || phone.isEmpty() || birthDate.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos, incluindo o Gênero.", Toast.LENGTH_LONG).show();
            return;
        }


        User newUser = new User(name, email, password, cpf, phone, birthDate, gender);


        boolean isInserted = dbHelper.addUser(newUser);

        if (isInserted) {
            Toast.makeText(this, "Cadastro realizado com sucesso! Faça login.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(CadastrarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Erro no cadastro. O e-mail pode já estar em uso.", Toast.LENGTH_LONG).show();
        }
    }
}
