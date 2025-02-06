package dev.mlds.listadecompras.model

data class Item(
    val name: String = "",
    val value: Double = 0.0,
    val userLocation: String? = "",
    var checked: Boolean = false
)