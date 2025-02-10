package dev.mlds.listadecompras

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dev.mlds.listadecompras.model.HistoricoPreco
import dev.mlds.listadecompras.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetalheViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference

    private val _historico = MutableStateFlow<List<HistoricoPreco>>(emptyList())
    val historico: StateFlow<List<HistoricoPreco>> = _historico

    private val _item = MutableStateFlow<Item?>(null)
    val item: StateFlow<Item?> = _item

    fun getItemById(itemId: String) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val itemRef = database.child("listas").child(uid).child(itemId)
            itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(Item::class.java)
                        Log.d("ListaDeComprasViewModel", "Item encontrado: $item")
                        _item.value = item
                    } else {
                        Log.e("ListaDeComprasViewModel", "Item com ID $itemId não encontrado.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ListaDeComprasViewModel.getItemById()", error.message)
                }
            })
        } else {
            println("Erro: Usuário não autenticado.")
        }
    }

    fun atualizarPreco(itemId: String, novoPreco: Double) {
        val userId = auth.currentUser?.uid ?: return
        val itemRef = database.child("listas").child(userId).child(itemId)

        itemRef.child("value").get().addOnSuccessListener { snapshot ->
            val precoAtual = snapshot.getValue(Double::class.java) ?: return@addOnSuccessListener

            // Só adiciona ao histórico se o preço realmente mudou
            if (precoAtual != novoPreco) {
                val historicoRef = itemRef.child("historico_precos").push()

                val historico = mapOf(
                    "preco" to precoAtual,
                    "data" to System.currentTimeMillis()
                )

                historicoRef.setValue(historico)
            }

            // Atualiza o novo preço do item
            itemRef.child("value").setValue(novoPreco)
        }
    }

    fun carregarHistorico(itemId: String) {
        obterHistoricoPreco(itemId) { lista ->
            _historico.value = lista
        }
    }

    fun obterHistoricoPreco(itemId: String, callback: (List<HistoricoPreco>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val historicoRef = database.child("listas").child(userId)
            .child(itemId).child("historico_precos")

        historicoRef.orderByChild("data").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val historicoList = snapshot.children.mapNotNull { it.getValue(HistoricoPreco::class.java) }
                callback(historicoList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao obter histórico de preços", error.toException())
            }
        })
    }
}