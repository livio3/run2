package com.example.livio3.run2;

import java.time.LocalDate;

/**
 * Created by livio3 on 06/07/18.
 */

public class Runner {
    private   int id_runner;
    private   String name;
    private  String surname;
    private  char sex;
    private  String username;
    private  String password;
    private  String birth_date;

    public Runner() {}

    public Runner( String name, String surname, char sex, String username, String password, String birth_date) {

        this.name = name;
        this.surname = surname;
        this.setSex(sex);
        this.username = username;
        this.password = password;
        this.birth_date = birth_date;
    }

    public int getId_runner() {
        return id_runner;
    }

    public void setId_runner(int id_runner) {
        this.id_runner = id_runner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char getSex() {

        return sex;
    }

    public void setSex(char sex) {
        if(sex == 'M' || sex == 'F')
            this.sex = sex;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
