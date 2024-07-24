package de.lrprojects.tempserver.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
data class Sensor(
    @Id
    var id: String? = null,
    @ManyToOne
    var type: Type? = null,
    @OneToMany
    var categories: List<Category>? = null,
    var sensorNick: String? = null
)