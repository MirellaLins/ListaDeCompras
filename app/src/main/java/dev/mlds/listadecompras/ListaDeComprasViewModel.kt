package dev.mlds.listadecompras

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dev.mlds.listadecompras.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListaDeComprasViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    init {
        authenticateAndFetchItems()
    }

    private fun authenticateAndFetchItems() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid = user?.uid
                if (uid != null) {
                    fetchItems(uid)
                } else {
                    println("Erro: UID do usuário é nulo.")
                }
            } else {
                println("Falha na autenticação: ${task.exception?.message}")
            }
        }
    }

    private fun fetchItems(uid: String) {
        val userItemsRef = database.child("listas").child(uid)
        userItemsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = snapshot.children.mapNotNull { it.getValue(Item::class.java) }
                _items.value = itemList
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate erros aqui se necessário
            }
        })
    }

    fun addItem(name: String, value: Double, userLocation: String?) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val newItem = Item(name = name, value = value, userLocation = userLocation)
            val userItemsRef = database.child("listas").child(uid)
            val newItemRef = userItemsRef.push()
            newItemRef.setValue(newItem)
        } else {
            println("Erro: Usuário não autenticado.")
        }
    }

    fun removeItem(item: Item) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val userItemsRef = database.child("listas").child(uid)
            userItemsRef.orderByChild("name").equalTo(item.name)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { it.ref.removeValue() }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("Erro ao remover item: ${error.message}")
                    }
                })
        } else {
            println("Erro: Usuário não autenticado.")
        }
    }

    fun toggleItemChecked(item: Item) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val userItemsRef = database.child("listas").child(uid)
            userItemsRef.orderByChild("name").equalTo(item.name)
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

                    override fun onCancelled(error: DatabaseError) {
                        println("Erro ao alternar estado do item: ${error.message}")
                    }
                })
        } else {
            println("Erro: Usuário não autenticado.")
        }
    }

    fun toggleAllItems(items: List<Item>, selectAll: Boolean) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val userItemsRef = database.child("listas").child(uid)
            items.forEach { item ->
                userItemsRef.orderByChild("name").equalTo(item.name)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach {
                                it.ref.child("checked").setValue(selectAll)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Erro ao alternar estado de todos os itens: ${error.message}")
                        }
                    })
            }
        } else {
            println("Erro: Usuário não autenticado.")
        }
    }
}
