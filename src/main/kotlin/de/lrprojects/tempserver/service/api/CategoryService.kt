package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Category

interface CategoryService {

    fun getAllCategories(): List<Category>

    fun getCategoryById(id: Long): Category?

    fun createCategory(category: Category): Category

    fun updateCategory(category: Category): Category

    fun deleteCategory(id: Long)

}