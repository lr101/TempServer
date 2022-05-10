package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.JDBC;
import com.example.SpringServer.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
public class RestControllerSensors {

    @Autowired
    SensorRepository sensorRepo;
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
