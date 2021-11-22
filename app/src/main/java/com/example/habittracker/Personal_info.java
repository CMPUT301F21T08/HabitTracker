package com.example.habittracker;

public class Personal_info {
    private String name;
    private String email;
    private String gender;
    private int age;
    private String uid;


    public Personal_info(){}

    public Personal_info(String name, String email, String gender, int age, String uid) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }


}
