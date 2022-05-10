package com.example.SpringServer.Entities;

import java.sql.Timestamp;

public class Entry {

    public Entry(Long row_id, double entryValue, Timestamp date) {
        this.row_id = row_id;
        this.entryValue = entryValue;
        this.date = date;
    }

    private Long row_id;
    private double entryValue;
    private Timestamp date;

    public void setRow_id(Long row_id) {
        this.row_id = row_id;
    }

    public Long getRow_id() {
        return row_id;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public void setEntryValue(double entryValue) {
        this.entryValue = entryValue;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return entryValue + " unit";
    }
}
