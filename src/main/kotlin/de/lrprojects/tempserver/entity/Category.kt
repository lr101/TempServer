package de.lrprojects.tempserver.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id


@Entity
data class Category(
    @Id
    val id: Long? = null,
    var name: String? = null,
    var description: String? = null
)
