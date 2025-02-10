package dev.mlds.listadecompras.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.mlds.listadecompras.DetalheViewModel
import dev.mlds.listadecompras.ListaDeComprasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheItemScreen(
    itemId: String,
    navController: NavController,
    detalheViewModel: DetalheViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val item by detalheViewModel.item.collectAsState(initial = null)
    var novoPreco by remember { mutableStateOf("") }

    // Atualiza o campo de preço quando o item é carregado
    LaunchedEffect(item) {
        item?.let { novoPreco = it.value.toString() }
    }

    LaunchedEffect(itemId) {
        detalheViewModel.getItemById(itemId)
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (item == null) {
                // Centraliza o indicador de progresso
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val i = item!!
                Text(text = "Nome: ${i.name}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Valor atual: R$ %.2f".format(i.value), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = novoPreco,
                    onValueChange = { novoPreco = it },
                    label = { Text("Novo Preço") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        novoPreco.toDoubleOrNull()?.let { precoDouble ->
                            detalheViewModel.atualizarPreco(i.id!!, precoDouble)
                        } ?: run {
                            // Pode exibir um erro visual se necessário
                        }
                    }
                ) {
                    Text("Salvar Preço")
                }
            }
        }
    }
}
