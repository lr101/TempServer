package de.lrprojects.tempserver.service.impl

import de.lrprojects.tempserver.entity.Category
import de.lrprojects.tempserver.repository.CategoryRepository
import de.lrprojects.tempserver.service.api.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {

    override fun getAllCategories(): List<Category> = categoryRepository.findAll()

    override fun getCategoryById(id: Long): Category? = categoryRepository.findById(id).orElse(null)

    override fun createCategory(category: Category): Category = categoryRepository.save(category)

    override fun updateCategory(category: Category): Category = categoryRepository.save(category)

    override fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }
}