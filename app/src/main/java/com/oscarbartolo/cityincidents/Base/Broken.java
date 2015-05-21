package com.oscarbartolo.cityincidents.Base;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Oscar on 03/05/2015.
 */
public class Broken {
    private int id;
    private Timestamp date;
    private List<Person> person;
    private List<Incident> incident;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public List<Incident> getIncident() {
        return incident;
    }

    public void setIncident(List<Incident> incident) {
        this.incident = incident;
    }
}
