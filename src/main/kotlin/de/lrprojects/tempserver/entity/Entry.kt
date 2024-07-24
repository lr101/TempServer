package de.lrprojects.tempserver.entity

import java.sql.Timestamp

data class Entry(
    var timestamp: Timestamp? = null,
    var value: Double? = null,
    var sensorId: String? = null
)