package dev.mlds.listadecompras.model

data class Item(
    val name: String = "",
    val value: Double = 0.0,
    var checked: Boolean = false
)