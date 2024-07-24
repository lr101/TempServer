package de.lrprojects.tempserver.converter

import de.lrprojects.tempserver.entity.Category
import de.lrprojects.tempserver.model.CategoryDto

fun Category.toDto() = CategoryDto().also {
    it.id = this.id
    it.name = this.name
    it.description = this.description
}

fun CategoryDto.toEntity() = Category(this.id).also {
    it.name = this.name
    it.description = this.description
}
