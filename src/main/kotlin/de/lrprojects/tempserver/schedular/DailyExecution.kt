package de.lrprojects.tempserver.schedular

import de.lrprojects.tempserver.service.api.EntryService
import de.lrprojects.tempserver.service.api.SensorService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.SQLException
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Component
@EnableScheduling
class DailyExecution(
    private val sensorService: SensorService,
    private val entryService: EntryService
) {

    @Scheduled(cron = "0 0 0 * * *")
    @Throws(SQLException::class)
    fun moveToBackup() {
        val sensors = sensorService.getAllSensors()
        val date = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
        for (sensor in sensors) {
            entryService.deleteEntries(sensor.id!!, date, null)
        }
    }

    companion object {
        private const val DAYS = 7
    }
}