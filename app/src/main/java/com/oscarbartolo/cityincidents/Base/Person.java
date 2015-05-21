package com.oscarbartolo.cityincidents.Base;

/**
 * Created by Oscar on 03/05/2015.
 */
public class Person {
    private int id;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String pass;
    private String city;
    private String address;
    private byte[] image;
    private Incident incident;
    private Broken broken;
    private Fixed fixed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public Broken getBroken() {
        return broken;
    }

    public void setBroken(Broken broken) {
        this.broken = broken;
    }

    public Fixed getFixed() {
        return fixed;
    }

    public void setFixed(Fixed fixed) {
        this.fixed = fixed;
    }
}
