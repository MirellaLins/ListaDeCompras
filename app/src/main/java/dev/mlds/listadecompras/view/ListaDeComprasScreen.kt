import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mlds.listadecompras.ListaDeComprasViewModel

@Composable
fun ListaDeComprasScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: ListaDeComprasViewModel = viewModel(),
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("cadastro") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Item")
            }
        },
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(viewModel.items) { item ->
                    ItemRow(
                        item = item,
                        onItemChecked = { viewModel.toggleItemChecked(item) },
                        onItemRemoved = { viewModel.removeItem(item) }
                    )
                }
            }
        }
    }
}
