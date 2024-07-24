package de.lrprojects.tempserver.converter

import de.lrprojects.tempserver.entity.Entry
import de.lrprojects.tempserver.model.EntryDto
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun Entry.toDto() = EntryDto().also {
    it.sensorId = this.sensorId
    it.value = this.value
    it.timestamp = OffsetDateTime.of(this.timestamp!!.toLocalDateTime(), ZoneOffset.UTC)
}

fun EntryDto.toEntity() = Entry().also {
    it.sensorId = this.sensorId
    it.value = this.value
    it.timestamp = Timestamp.from(this.timestamp.toInstant())
}
