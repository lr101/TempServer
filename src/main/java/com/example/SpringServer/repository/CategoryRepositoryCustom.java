package com.example.SpringServer.repository;

import com.example.SpringServer.Entities.Category;

public interface CategoryRepositoryCustom {
    public Category findByCategoryId(Long id);
    public Category updateCategory(Category category);
    public void deleteCategory(Long sensorCategory);
}
