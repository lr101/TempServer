package de.lrprojects.tempserver.repository

import de.lrprojects.tempserver.entity.Sensor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorRepository : JpaRepository<Sensor, String>