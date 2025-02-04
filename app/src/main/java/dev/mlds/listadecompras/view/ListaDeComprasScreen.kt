package dev.mlds.listadecompras.view

import ItemRow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.mlds.listadecompras.ListaDeComprasViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListaDeComprasScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: ListaDeComprasViewModel = viewModel(),
) {
    val items by viewModel.items.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("cadastro") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Item")
            }
        },
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Listagem dos produtos", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(items) { item ->
                    ItemRow(
                        item = item,
                        onItemChecked = { viewModel.toggleItemChecked(item) },
                        onItemRemoved = { viewModel.removeItem(item) },
                        modifier = modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}
