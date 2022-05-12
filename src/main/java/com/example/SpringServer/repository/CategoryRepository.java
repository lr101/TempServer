package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoryRepository extends CrudRepository<Category, Long>, CategoryRepositoryCustom {}