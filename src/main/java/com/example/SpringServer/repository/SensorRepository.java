package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends CrudRepository<Id, Long>, SensorRepositoryCustom {}

