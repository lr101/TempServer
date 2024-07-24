package de.lrprojects.tempserver.Schedular

import de.lrprojects.tempserver.service.api.EntryService
import de.lrprojects.tempserver.service.api.SensorService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.SQLException

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
        val date = System.currentTimeMillis() - DAYS * 24 * 60 * 60 * 1000
        for (sensor in sensors) {
            entryService.deleteEntries(sensor.id!!, date, null)
        }
    }

    companion object {
        private const val DAYS = 7
    }
}