package dev.mlds.listadecompras

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.mlds.listadecompras.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListaDeComprasViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("itens")

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    init {
        fetchItems()
    }

    private fun fetchItems() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = snapshot.children.mapNotNull { it.getValue(Item::class.java) }
                _items.value = itemList
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate erros aqui se necessário
            }
        })
    }

    fun addItem(name: String, value: Double) {
        val newItem = Item(name, value)
        val newItemRef = database.push()
        newItemRef.setValue(newItem)
    }

    fun removeItem(item: Item) {
        database.orderByChild("name").equalTo(item.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { it.ref.removeValue() }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun toggleItemChecked(item: Item) {
        database.orderByChild("name").equalTo(item.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val currentChecked = it.child("checked").getValue(Boolean::class.java) ?: false
                        it.ref.child("checked").setValue(!currentChecked).addOnSuccessListener {
                            // Atualiza localmente APÓS a mudança no Firebase
                            _items.value = _items.value.map { i ->
                                if (i.name == item.name) i.copy(checked = !currentChecked) else i
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
