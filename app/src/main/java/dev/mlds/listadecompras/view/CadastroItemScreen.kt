package dev.mlds.listadecompras.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CadastroItemScreen(navController: NavController, onItemAdded: (String, Double) -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var itemValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Cadastro de Item", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Nome do item") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = itemValue,
            onValueChange = { itemValue = it },
            label = { Text("Valor (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val value = itemValue.toDoubleOrNull() ?: 0.0
                if (itemName.isNotBlank() && value > 0) {
                    onItemAdded(itemName, value)
                    navController.popBackStack() // Voltar para a lista
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }
    }
}
