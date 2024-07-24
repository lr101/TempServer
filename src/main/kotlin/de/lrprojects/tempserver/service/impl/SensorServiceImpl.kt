package de.lrprojects.tempserver.service.impl

import de.lrprojects.tempserver.entity.Sensor
import de.lrprojects.tempserver.repository.SensorRepository
import de.lrprojects.tempserver.service.api.SensorService
import org.springframework.stereotype.Service

@Service
class SensorServiceImpl(
    private val sensorRepository: SensorRepository,
): SensorService {
    override fun getAllSensors(): List<Sensor> = sensorRepository.findAll()

    override fun getSensorById(sensorId: String): Sensor? = sensorRepository.findById(sensorId).orElse(null)

    override fun createSensor(sensor: Sensor): Sensor {
        val savedSensor = sensorRepository.save(sensor)
        return savedSensor
    }

    override fun updateSensor(sensor: Sensor): Sensor = sensorRepository.save(sensor)

    override fun deleteSensor(sensorId: String) {
        sensorRepository.deleteById(sensorId)
    }
}