package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Type
import de.lrprojects.tempserver.model.TypeDto

interface TypeService {
    fun getAllTypes(): List<Type>

    fun getTypeById(id: Long): Type?

    fun getTypeByName(name: String): Type?

    fun createType(type: TypeDto): Type

    fun updateType(type: TypeDto): Type

    fun deleteType(id: Long)
}
