package de.lrprojects.tempserver.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany


@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    @ManyToMany(mappedBy = "categories")
    var sensors: List<Sensor> = emptyList()
)
