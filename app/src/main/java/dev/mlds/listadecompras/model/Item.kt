package dev.mlds.listadecompras.model

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val value: Double = 0.0,
    val userLocation: String? = "",
    var checked: Boolean = false
)