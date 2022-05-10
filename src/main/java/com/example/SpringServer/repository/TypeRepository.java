package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TypeRepository extends CrudRepository<Type, Long>, TypeRepositoryCustom {}