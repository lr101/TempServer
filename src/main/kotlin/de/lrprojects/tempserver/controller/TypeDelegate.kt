package de.lrprojects.tempserver.controller

import de.lrprojects.tempserver.api.SensorTypesApiDelegate
import de.lrprojects.tempserver.converter.toDto
import de.lrprojects.tempserver.model.TypeDto
import de.lrprojects.tempserver.service.api.TypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class TypeDelegate(
    private val typeService: TypeService
): SensorTypesApiDelegate {

    override fun getSensorTypes(): ResponseEntity<MutableList<TypeDto>> {
        return ResponseEntity.ok(typeService.getAllTypes().map { it.toDto() }.toMutableList())
    }

    override fun getSensorTypeById(id: Long): ResponseEntity<TypeDto> {
        return ResponseEntity.ok(typeService.getTypeById(id)!!.toDto())
    }

    override fun deleteSensorType(sensorTypeId: Long?): ResponseEntity<Void> {
        typeService.deleteType(sensorTypeId!!)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun postSensorType(typeDto: TypeDto): ResponseEntity<TypeDto> {
        return ResponseEntity(typeService.createType(typeDto).toDto(), HttpStatus.CREATED)
    }

    override fun putSensorType(typeDto: TypeDto?): ResponseEntity<TypeDto> {
        return ResponseEntity(typeService.updateType(typeDto!!).toDto(), HttpStatus.OK)
    }
}