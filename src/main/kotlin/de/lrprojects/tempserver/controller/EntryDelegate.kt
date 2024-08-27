package de.lrprojects.tempserver.controller

import de.lrprojects.tempserver.api.SensorEntriesApiDelegate
import de.lrprojects.tempserver.converter.toDto
import de.lrprojects.tempserver.converter.toEntity
import de.lrprojects.tempserver.model.EntryDto
import de.lrprojects.tempserver.service.api.EntryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class EntryDelegate(
    private val entryService: EntryService
): SensorEntriesApiDelegate {

    override fun deleteEntry(sensorId: String, date1: OffsetDateTime, date2: OffsetDateTime): ResponseEntity<Void> {
        entryService.deleteEntries(sensorId, date1, date2)
        return ResponseEntity.ok().build()
    }

    override fun getEntries(
        sensorId: String,
        date1: OffsetDateTime?,
        date2: OffsetDateTime?,
        limit: Int?,
        interval: Int?
    ): ResponseEntity<MutableList<EntryDto>>? {
        return ResponseEntity.ok(entryService.getEntries(sensorId, date1, date2, limit, interval).map { it.toDto() }.toMutableList())
    }

    override fun postEntry(sensorId: String, entryDto: EntryDto): ResponseEntity<Void> {
        entryService.createEntry(sensorId, entryDto.toEntity())
        return ResponseEntity(HttpStatus.CREATED)
    }
}

