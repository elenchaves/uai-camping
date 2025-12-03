package com.antonia.uaicamping;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;

    private String cpf;
    private String phone;
    private String birthDate;
    private String gender;
    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String cpf, String phone, String birthDate, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public User(int id, String name, String email, String password, String cpf, String phone, String birthDate, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
    }


    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
