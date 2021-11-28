package com.example.habittracker;

public class Personal_info {
    private String name;
    private String email;
    private String gender;
    private int age;
    private String localImagePath;
    private int genderId = -1;
    private String uid;

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private String downloadUrl; // This stores the url directing to the photo stored in firebase, not set by any constructor



    public Personal_info(){}

    public Personal_info(String name, String email, String gender, int age) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.localImagePath = null;
    }

    public Personal_info(String name, String email, int genderId, int age, String localImagePath) {
        this.name = name;
        this.email = email;
        this.genderId = genderId;

        this.age = age;
        this.localImagePath = null;
        this.localImagePath = localImagePath;
    }


    public Personal_info(String name, String email, String gender, int age, String localImagePath) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.localImagePath = localImagePath;
    }

    public Personal_info(String email) {
        this.email = email;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
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
