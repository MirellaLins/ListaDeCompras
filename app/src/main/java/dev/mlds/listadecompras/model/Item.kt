package dev.mlds.listadecompras.model

data class Item(
    val id: Int,
    val name: String,
    val value: Double,
    var isChecked: Boolean = false
)