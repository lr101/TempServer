package de.lrprojects.tempserver.controller

import de.lrprojects.tempserver.api.SensorCategoriesApiDelegate
import de.lrprojects.tempserver.converter.toDto
import de.lrprojects.tempserver.converter.toEntity
import de.lrprojects.tempserver.model.CategoryDto
import de.lrprojects.tempserver.service.api.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class CategoryDelegate(
    private val categoryService: CategoryService
): SensorCategoriesApiDelegate {

    override fun getAllSensorCategories(): ResponseEntity<MutableList<CategoryDto>> {
        return ResponseEntity.ok(categoryService.getAllCategories().map { it.toDto() }.toMutableList())
    }

    override fun createSensorCategory(categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity(categoryService.createCategory(categoryDto.toEntity()).toDto(), HttpStatus.CREATED)
    }

    override fun deleteSensorCategory(categoryId: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.ok().build()
    }

    override fun getSensorCategoryById(categoryId: Long): ResponseEntity<CategoryDto> {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId)?.toDto())
    }

    override fun updateSensorCategory(categoryId: Long, categoryDto: CategoryDto): ResponseEntity<CategoryDto>? {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, categoryId).toDto())
    }

}