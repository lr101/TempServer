package de.lrprojects.tempserver.service.api

import de.lrprojects.tempserver.entity.Entry

interface EntryService {

    fun getEntries(sensorId: String, date1: Long?, date2: Long?, limit: Int?): List<Entry>

    fun createEntry(sensorId: String, entry: Entry)

    fun deleteEntries(sensorId: String, date1: Long?, date2: Long?)

}