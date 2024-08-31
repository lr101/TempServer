package de.lrprojects.tempserver.controller

import de.lrprojects.tempserver.api.SensorsApiDelegate
import de.lrprojects.tempserver.converter.toDto
import de.lrprojects.tempserver.converter.toEntity
import de.lrprojects.tempserver.model.SensorDto
import de.lrprojects.tempserver.service.api.SensorService
import de.lrprojects.tempserver.service.api.TypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class SensorDelegate(
    private val sensorService: SensorService,
    private val typeService: TypeService
): SensorsApiDelegate {

    override fun getSensors(): ResponseEntity<MutableList<SensorDto>> {
        return ResponseEntity.ok(sensorService.getAllSensors().map { it.toDto() }.toMutableList())
    }

    override fun deleteSensor(sensorId: String): ResponseEntity<Void> {
        sensorService.deleteSensor(sensorId)
        return ResponseEntity.ok().build()
    }

    override fun getSensorById(sensorId: String): ResponseEntity<SensorDto> {
        return ResponseEntity.ok(sensorService.getSensorById(sensorId)?.toDto())
    }

    override fun postSensor(sensorDto: SensorDto): ResponseEntity<SensorDto> {
        return ResponseEntity(sensorService.createSensor(sensorDto.toEntity(typeService)).toDto(), HttpStatus.CREATED)
    }

    override fun putSensor(sensorId: String, sensorDto: SensorDto): ResponseEntity<SensorDto>? {
        return ResponseEntity.ok(sensorService.updateSensor(sensorDto, sensorId).toDto())
    }
}
