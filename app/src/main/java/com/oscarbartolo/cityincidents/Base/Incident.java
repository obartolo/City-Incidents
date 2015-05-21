package com.oscarbartolo.cityincidents.Base;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Oscar on 03/05/2015.
 */
public class Incident {
    private int id;
    private String description;
    private byte[] image;
    private Timestamp createdate;
    private Timestamp lastmodificationdate;
    private String latitude;
    private String longitude;
    private String address;
    private List<Person> person;
    private Broken broken;
    private Fixed fixed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Timestamp getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public Timestamp getLastmodificationdate() {
        return lastmodificationdate;
    }

    public void setLastmodificationdate(Timestamp lastmodificationdate) {
        this.lastmodificationdate = lastmodificationdate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
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
