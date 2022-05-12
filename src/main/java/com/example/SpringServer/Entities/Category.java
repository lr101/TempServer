package com.example.SpringServer.Entities;


import javax.persistence.Id;
import javax.persistence.*;

@Entity
public class Category {
    @JoinColumn(name = "category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sensor_category", nullable = false)
    private String sensorCategory = "All Sensors";

    public Long getId() {
        return id;
    }

    public String getSensorCategory() {return sensorCategory;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setSensorCategory(String sensorCategory) {
        this.sensorCategory = sensorCategory;
    }

    @Override
    public String toString() {
        return "id: "+  id + " name:" + sensorCategory;
    }

}
