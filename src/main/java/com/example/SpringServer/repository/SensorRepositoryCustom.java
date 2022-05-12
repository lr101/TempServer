package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Category;
import com.example.SpringServer.Entities.Id;

import java.util.ArrayList;
import java.util.Set;

public interface SensorRepositoryCustom {
    public Id findById(String sensorId);
    public String findSensorNickById(String sensorId);
    public String findSensorTypeById(String sensorId);
    public Set<Category> findSensorCategoriesById(String sensorId);
    public Id updateSensor(Id sensor);
    public void deleteSensor(String sensorId);
}
