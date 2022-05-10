package com.example.SpringServer.Entities;


import javax.persistence.*;
import javax.persistence.Id;

@Entity
public class Type {
    @JoinColumn(name = "type_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sensor_type")
    private String sensorType = "Default";

    @Column(name = "unit")
    private String unit = "dUnit";

    @Column(name = "repetitions")
    private int repetitions = 10;

    @Column(name = "sleep_time")
    private int sleepTime = 10;

    public Long getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public String getSensorType() {return sensorType;}

    public int getSleepTime() {
        return sleepTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public void setSleepTime (int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "id: "+  id + " name:" + sensorType + " repetitions: " + repetitions + " unit: " + unit + " sleepTime: " +sleepTime;
    }

}
