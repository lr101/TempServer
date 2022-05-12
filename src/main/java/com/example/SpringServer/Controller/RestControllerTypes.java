package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.JDBC;
import com.example.SpringServer.Entities.Type;
import com.example.SpringServer.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestControllerTypes {

    @Autowired
    TypeRepository typeRepository;

    JDBC jdbc = new JDBC();

    @EventListener(ApplicationReadyEvent.class)
    public void setupType() {
        if (typeRepository.findByTypeId(0L) == null) {
            Type defaultType = new Type();
            typeRepository.save(defaultType);
            jdbc.updateType( 0L, defaultType.getId());
        }
    }

    //working
    @GetMapping(value = "/sensors/types/")
    public ArrayList<Type> getSensorTypeId () {
        return (ArrayList<Type>) typeRepository.findAll();
    }

    @GetMapping(value = "/sensors/types/{id}")
    public Type getSensorTypeIdById (@PathVariable("id") Long id) {
        return typeRepository.findByTypeId(id);
    }

    //working
    @PutMapping("/sensors/types/")
    public Type putSensor(@RequestBody Type type) {
        if (type.getId() == 0) {
            throw new IllegalArgumentException();
        }
        return typeRepository.save(typeRepository.updateType(type));
    }

    //working
    @PostMapping("/sensors/types/")
    public Type postSensor(@RequestBody Type type) {
        if (type.getId() == 0) {
            type.setId((long) -1);
        }
        return typeRepository.save(type);
    }

    //working
    //TODO Cascading delete into Id table
    @DeleteMapping("/sensors/types/{sensorTypeId}")
    public void deleteSensor(@PathVariable("sensorTypeId") Long sensorTypeId) {
        typeRepository.deleteType(sensorTypeId);
    }
}
