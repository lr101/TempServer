package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Category;
import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.JDBC;
import com.example.SpringServer.repository.CategoryRepository;
import com.example.SpringServer.repository.SensorRepository;
import com.example.SpringServer.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class RestControllerSensors {

    @Autowired
    SensorRepository sensorRepo;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TypeRepository typeRepo;

    JDBC jdbc = new JDBC();

    //working
    @GetMapping("/sensors/id")
    public ArrayList<Id> getSensors() {
        return (ArrayList<Id>) sensorRepo.findAll();
    }

    //working
    @GetMapping(value = "/sensors/id/{sensorId}")
    public Id getSensorById (@PathVariable("sensorId") String sensorId) {
        return sensorRepo.findById(sensorId);
    }

    //working
    @GetMapping(value = "/sensors/id/{sensorId}/sensor_nick")
    public Map<String, String> getSensorNickById (@PathVariable("sensorId") String sensorId) {
        Map<String, String> map = new HashMap<>();
        map.put("sensor_nick", sensorRepo.findSensorNickById(sensorId));
        return map;
    }

    //working
    @GetMapping(value = "/sensors/id/{sensorId}/sensor_type")
    public Map<String, String> getSensorTypeIdById (@PathVariable("sensorId") String sensorId) {
        Map<String, String> map = new HashMap<>();
        map.put("sensor_type", sensorRepo.findSensorTypeById(sensorId));
        return map;
    }

    //working
    @PutMapping("/sensors/id/")
    public Id putSensor(@RequestBody Id sensor) {
        return sensorRepo.save(sensorRepo.updateSensor(sensor));
    }

    //working
    @PostMapping("/sensors/id/")
    public Id postSensor(@RequestBody Id sensor) {
        if (sensor.getSensorType() == null) {
            sensor.setSensorType(typeRepo.findByTypeId(0L));
        }
        if (sensor.getSensorCategory() == null) {
            Set<Category> c = new HashSet<>();
            c.add(categoryRepository.findByCategoryId(0L));
            sensor.setSensorCategory(c);
        }
        Id id = sensorRepo.save(sensor);
        jdbc.createEntryTable(sensor.getSensorId());
        return id;
    }

    //working
    @DeleteMapping("/sensors/id/{sensorId}")
    public void deleteSensor(@PathVariable("sensorId") String sensorId) {
        jdbc.dropTable(sensorId);
        sensorRepo.deleteSensor(sensorId);
    }
}
