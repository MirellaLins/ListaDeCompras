package dev.mlds.listadecompras

import ListaDeComprasScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import dev.mlds.listadecompras.ui.theme.ListaDeComprasTheme
import dev.mlds.listadecompras.view.CadastroItemScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListaDeComprasTheme {
                val navController = rememberNavController()
                val viewModel: ListaDeComprasViewModel = viewModel()

                NavHost(navController, startDestination = "lista") {
                    composable("lista") {
                        ListaDeComprasScreen(modifier = Modifier, navController, viewModel)
                    }
                    composable("cadastro") {
                        CadastroItemScreen(navController) { name, value ->
                            viewModel.addItem(name, value)
                        }
                    }
                }
            }
        }
    }
}