package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Id;

public interface SensorRepositoryCustom {
    public Id findById(String sensorId);
    public String findSensorNickById(String sensorId);
    public String findSensorTypeById(String sensorId);
    public Id updateSensor(Id sensor);
    public void deleteSensor(String sensorId);
}
