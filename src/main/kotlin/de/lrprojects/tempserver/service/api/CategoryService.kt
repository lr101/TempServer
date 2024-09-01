package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Category
import de.lrprojects.tempserver.model.CategoryDto

interface CategoryService {

    fun getAllCategories(): List<Category>

    fun getCategoryById(id: Long): Category?

    fun createCategory(category: Category): Category

    fun updateCategory(categoryDto: CategoryDto, id: Long): Category

    fun deleteCategory(id: Long)

}