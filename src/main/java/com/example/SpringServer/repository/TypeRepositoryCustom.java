package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.Type;

public interface TypeRepositoryCustom {
    public Type findByTypeId(Long id);
    public Type updateType(Type type);
    public void deleteType(Long sensorType);
}
