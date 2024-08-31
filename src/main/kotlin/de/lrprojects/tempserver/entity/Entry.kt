package de.lrprojects.tempserver.entity

import java.time.OffsetDateTime

data class Entry(
    var timestamp: OffsetDateTime? = null,
    var value: Double? = null,
)