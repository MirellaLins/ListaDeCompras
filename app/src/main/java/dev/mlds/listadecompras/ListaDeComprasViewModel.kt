package dev.mlds.listadecompras

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dev.mlds.listadecompras.model.Item

class ListaDeComprasViewModel : ViewModel() {
    private val _items = mutableStateListOf<Item>()
    val items: List<Item> get() = _items

    fun addItem(name: String) {
        val newItem = Item(id = _items.size + 1, name = name)
        _items.add(newItem)
    }

    fun toggleItemChecked(item: Item) {
        val index = _items.indexOf(item)
        if (index != -1) {
            _items[index] = item.copy(isChecked = !item.isChecked)
        }
    }

    fun removeItem(item: Item) {
        _items.remove(item)
    }
}