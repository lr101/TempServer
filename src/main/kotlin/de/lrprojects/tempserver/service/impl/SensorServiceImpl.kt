package de.lrprojects.tempserver.service.impl

import de.lrprojects.tempserver.entity.Sensor
import de.lrprojects.tempserver.model.SensorDto
import de.lrprojects.tempserver.repository.CategoryRepository
import de.lrprojects.tempserver.repository.SensorRepository
import de.lrprojects.tempserver.repository.TypeRepository
import de.lrprojects.tempserver.service.api.SensorService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SensorServiceImpl(
    private val sensorRepository: SensorRepository,
    private val typeRepository: TypeRepository,
    private val categoryRepository: CategoryRepository
): SensorService {
    override fun getAllSensors(): List<Sensor> = sensorRepository.findAll()

    override fun getSensorById(sensorId: String): Sensor? = sensorRepository.findById(sensorId).orElse(null)

    override fun createSensor(sensor: Sensor): Sensor {
        val savedSensor = sensorRepository.save(sensor)
        return savedSensor
    }

    override fun updateSensor(sensor: SensorDto, sensorId: String): Sensor {
        val sensorToUpdate = sensorRepository.findById(sensorId).orElse(null)
        sensorToUpdate?.let {
            it.sensorNick = sensor.sensorNick
            it.type = sensor.sensorType?.let { typeRepository.findByIdOrNull(sensor.sensorType.id) }
            it.categories = sensor.categories?.map { c -> categoryRepository.findByIdOrNull(c.id)!! }
        }
        return sensorRepository.save(sensorToUpdate!!)
    }

    override fun deleteSensor(sensorId: String) {
        sensorRepository.deleteById(sensorId)
    }
}