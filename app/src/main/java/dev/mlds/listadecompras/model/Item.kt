package dev.mlds.listadecompras.model

data class Item(
    val id: Int,
    val name: String,
    var isChecked: Boolean = false
)