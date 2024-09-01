package de.lrprojects.tempserver.entity

import jakarta.persistence.*

@Entity
data class Sensor(
    @Id
    var id: String? = null,
    @ManyToOne
    var type: Type? = null,
    @ManyToMany
    @JoinTable(
        name = "sensor_categories",
        joinColumns = [JoinColumn(name = "sensor_id")],
        inverseJoinColumns = [JoinColumn(name = "categories_id")])
    var categories: List<Category>? = null,
    var sensorNick: String? = null
)