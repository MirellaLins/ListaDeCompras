package dev.mlds.listadecompras.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.mlds.listadecompras.ListaDeComprasViewModel
import dev.mlds.listadecompras.model.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheItemScreen(
    itemId: String,
    viewModel: ListaDeComprasViewModel, // <-- Para buscar o item pelo ID
    navController: NavController
) {
    val item by viewModel.item.collectAsState(initial = null)
    viewModel.getItemById(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        item?.let { i ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = "Nome: ${i.name}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Valor: R$ %.2f".format(i.value), style = MaterialTheme.typography.bodyLarge)
            }
        } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}