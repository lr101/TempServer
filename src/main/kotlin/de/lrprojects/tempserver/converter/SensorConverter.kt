package de.lrprojects.tempserver.converter

import de.lrprojects.tempserver.entity.Sensor
import de.lrprojects.tempserver.model.SensorDto
import de.lrprojects.tempserver.service.api.TypeService

fun Sensor.toDto() = SensorDto().also {
    it.sensorId = this.id
    it.sensorNick = this.sensorNick
    it.sensorType = this.type?.typeName
}

fun SensorDto.toEntity(typeService: TypeService) = Sensor().also {
    it.id = this.sensorId
    it.sensorNick = this.sensorNick
    it.type = this.sensorType?.let { type -> typeService.getTypeByName(type) }
}
