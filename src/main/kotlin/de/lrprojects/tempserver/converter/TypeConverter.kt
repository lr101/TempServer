package de.lrprojects.tempserver.converter

import de.lrprojects.tempserver.entity.Type
import de.lrprojects.tempserver.model.TypeDto


fun Type.toDto() = TypeDto().also {
    it.id = this.id
    it.typeName = this.typeName
    it.description = this.description
}

fun TypeDto.toEntity() = Type().also {
    it.id = this.id
    it.typeName = this.typeName
    it.description = this.description
}