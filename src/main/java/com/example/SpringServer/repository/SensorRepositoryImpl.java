package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Category;
import com.example.SpringServer.Entities.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Set;

public class SensorRepositoryImpl implements SensorRepositoryCustom {
    @Autowired
    @Lazy
    SensorRepository sensorRepository;

    @Override
    public Id findById(String sensorId) {
        ArrayList<Id> list = (ArrayList<Id>) sensorRepository.findAll();
        for (Id id : list) {
            if (id.getSensorId().equals(sensorId)) {
                return id;
            }
        }
        return null;
    }

    @Override
    public String findSensorNickById(String sensorId) {
        Id id = this.findById(sensorId);
        return id == null ? null : id.getSensorNick();
    }

    @Override
    public String findSensorTypeById(String sensorId) {
        Id id = this.findById(sensorId);
        return id == null ? null : id.getSensorType().getSensorType();
    }

    @Override
    public Set<Category> findSensorCategoriesById(String sensorId) {
        Id id = this.findById(sensorId);
        return id == null ? null : id.getSensorCategory();
    }

    @Override
    @Transactional
    public Id updateSensor(Id sensor) {
        Id id = this.findById(sensor.getSensorId());
        id.setSensorNick(sensor.getSensorNick());
        id.setSensorType(sensor.getSensorType());
        id.setSensorCategory(sensor.getSensorCategory());
        return id;
    }

    @Override
    public void deleteSensor(String sensorId) {
        sensorRepository.delete(this.findById(sensorId));
    }
}
