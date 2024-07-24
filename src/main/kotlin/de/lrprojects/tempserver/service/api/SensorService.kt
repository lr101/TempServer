package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Sensor

interface SensorService {
    fun getAllSensors(): List<Sensor>

    fun getSensorById(sensorId: String): Sensor?

    fun createSensor(sensor: Sensor): Sensor

    fun updateSensor(sensor: Sensor): Sensor

    fun deleteSensor(sensorId: String)
}
