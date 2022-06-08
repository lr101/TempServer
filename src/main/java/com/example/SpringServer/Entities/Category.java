package com.example.SpringServer.Entities;


import javax.persistence.Id;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @JoinColumn(name = "category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sensor_category", nullable = false)
    private String sensorCategory = "All Sensors";

    @Column(name = "test", nullable = false)
    private int test;

    @ManyToMany(mappedBy="categories")
    private Set<com.example.SpringServer.Entities.Id> ids = new HashSet<>();

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
