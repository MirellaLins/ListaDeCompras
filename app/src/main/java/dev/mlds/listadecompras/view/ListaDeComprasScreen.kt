package dev.mlds.listadecompras.view

import ItemRow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mlds.listadecompras.ListaDeComprasViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListaDeComprasScreen(
    modifier: Modifier,
    viewModel: ListaDeComprasViewModel = viewModel(),
) {
    var newItemName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Campo para adicionar novo item
        OutlinedTextField(
            value = newItemName,
            onValueChange = { newItemName = it },
            label = { Text("Novo item") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para adicionar item
        Button(
            onClick = {
                if (newItemName.isNotBlank()) {
                    viewModel.addItem(newItemName)
                    newItemName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de itens
        LazyColumn {
            items(viewModel.items, key = { it.id }) { item ->
                ItemRow(
                    item = item,
                    onItemChecked = { viewModel.toggleItemChecked(item) },
                    onItemRemoved = { viewModel.removeItem(item) },
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}