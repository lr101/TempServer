package de.lrprojects.tempserver.converter

import de.lrprojects.tempserver.entity.Sensor
import de.lrprojects.tempserver.model.SensorDto
import de.lrprojects.tempserver.service.api.CategoryService
import de.lrprojects.tempserver.service.api.TypeService
import org.jetbrains.kotlin.preloading.ProfilingInstrumenterExample.c
import org.jetbrains.kotlin.preloading.ProfilingInstrumenterExample.d

fun Sensor.toDto() = SensorDto().also {
    it.sensorId = this.id
    it.sensorNick = this.sensorNick
    it.sensorType = this.type?.toDto()
    it.categories = this.categories?.let { d -> d.map { c -> c.toDto()  } } ?: emptyList()
}

fun SensorDto.toEntity(typeService: TypeService, categoryService: CategoryService) = Sensor().also {
    it.id = this.sensorId
    it.sensorNick = this.sensorNick
    it.type = this.sensorType?.let { type -> typeService.getTypeById(type.id) }
    it.categories = this.categories?.map { c -> categoryService.getCategoryById(c.id)!! }
}
