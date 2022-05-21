package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Category;
import com.example.SpringServer.Entities.JDBC;
import com.example.SpringServer.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestControllerCategories {

    @Autowired
    CategoryRepository categoryRepository;

    JDBC jdbc = new JDBC();

    //TODO add a default category?
    /*@EventListener(ApplicationReadyEvent.class)
    public void setupCategory() {
        if (categoryRepository.findByCategoryId(0L) == null) {
            Category defaultCategory = new Category();
            categoryRepository.save(defaultCategory);
            jdbc.updateCategory( 0L, defaultCategory.getId());
        }
    }*/

    //working
    @GetMapping(value = "/sensors/categories/")
    public ArrayList<Category> getSensorCategoryId () {
        return (ArrayList<Category>) categoryRepository.findAll();
    }

    @GetMapping(value = "/sensors/categories/{id}")
    public Category getSensorCategoryIdById (@PathVariable("id") Long id) {
        return categoryRepository.findByCategoryId(id);
    }

    //working
    @PutMapping("/sensors/categories/")
    public Category putSensor(@RequestBody Category category) {
        if (category.getId() == 0) {
            throw new IllegalArgumentException("Not allowed to alter default category");
        }
        return categoryRepository.save(categoryRepository.updateCategory(category));
    }

    //working
    @PostMapping("/sensors/categories/")
    public Category postSensor(@RequestBody Category category) {
        if (category.getId() == 0) {
            category.setId((long) -1);
        }
        return categoryRepository.save(category);
    }

    //working
    //TODO Cascading delete into Id table
    @DeleteMapping("/sensors/categories/{sensorCategoryId}")
    public void deleteSensor(@PathVariable("sensorCategoryId") Long sensorCategoryId) {
        categoryRepository.deleteCategory(sensorCategoryId);
    }
}
