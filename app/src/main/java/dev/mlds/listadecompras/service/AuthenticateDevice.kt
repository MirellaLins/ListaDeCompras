package dev.mlds.listadecompras.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthenticateDevice {
    private val auth: FirebaseAuth = Firebase.auth

    fun authenticateDevice() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid = user?.uid // UID único do dispositivo
                println("Dispositivo autenticado com UID: $uid")
            } else {
                println("Falha na autenticação: ${task.exception?.message}")
            }
        }
    }
}