package de.lrprojects.tempserver.service.impl

import de.lrprojects.tempserver.converter.toEntity
import de.lrprojects.tempserver.entity.Type
import de.lrprojects.tempserver.model.TypeDto
import de.lrprojects.tempserver.repository.TypeRepository
import de.lrprojects.tempserver.service.api.TypeService
import org.springframework.stereotype.Service

@Service
class TypeServiceImpl(
    private val typeRepository: TypeRepository
): TypeService {
    override fun getAllTypes(): List<Type> = typeRepository.findAll()

    override fun getTypeById(id: Long): Type? = typeRepository.findById(id).orElse(null)

    override fun getTypeByName(name: String): Type? = typeRepository.findByTypeName(name)

    override fun createType(type: TypeDto): Type = typeRepository.save(type.toEntity())

    override fun updateType(type: TypeDto): Type = typeRepository.save(type.toEntity())

    override fun deleteType(id: Long) {
        typeRepository.deleteById(id)
    }
}
