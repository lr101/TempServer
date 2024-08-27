package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Entry
import java.time.OffsetDateTime

interface EntryService {

    fun getEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?, limit: Int?, interval: Int?): List<Entry>

    fun createEntry(sensorId: String, entry: Entry)

    fun deleteEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?)

}