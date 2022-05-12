package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @Autowired
    @Lazy
    CategoryRepository categoryRepository;

    @Override
    public Category findByCategoryId(Long id) {
        ArrayList<Category> list = (ArrayList<Category>) categoryRepository.findAll();
        for (Category category : list) {
            if (Objects.equals(category.getId(), id)) {
                return category;
            }
        }
        return null;
    }

    @Override
    public Category updateCategory(Category category) {
        if (category.getId() != 0) {
            Category t = this.findByCategoryId(category.getId());
            t.setSensorCategory(category.getSensorCategory());
            return t;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteCategory(Long sensorCategoryId) {
        if (sensorCategoryId != 0) {
            categoryRepository.delete(this.findByCategoryId(sensorCategoryId));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
