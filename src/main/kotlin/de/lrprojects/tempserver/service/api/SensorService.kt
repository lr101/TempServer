package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Sensor
import de.lrprojects.tempserver.model.SensorDto

interface SensorService {
    fun getAllSensors(): List<Sensor>

    fun getSensorById(sensorId: String): Sensor?

    fun createSensor(sensor: Sensor): Sensor

    fun updateSensor(sensor: SensorDto, sensorId: String): Sensor

    fun deleteSensor(sensorId: String)
}
