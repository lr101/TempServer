package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Objects;

public class TypeRepositoryImpl implements TypeRepositoryCustom {

    @Autowired
    @Lazy
    TypeRepository typeRepository;

    @Override
    public Type findByTypeId(Long id) {
        ArrayList<Type> list = (ArrayList<Type>) typeRepository.findAll();
        for (Type type : list) {
            if (Objects.equals(type.getId(), id)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public Type updateType(Type type) {
        if (type.getId() != 0) {
            Type t = this.findByTypeId(type.getId());
            t.setUnit(type.getUnit());
            t.setRepetitions(type.getRepetitions());
            t.setSleepTime(type.getSleepTime());
            t.setSensorType(type.getSensorType());
            return t;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteType(Long sensorTypeId) {
        if (sensorTypeId != 0) {
            typeRepository.delete(this.findByTypeId(sensorTypeId));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
