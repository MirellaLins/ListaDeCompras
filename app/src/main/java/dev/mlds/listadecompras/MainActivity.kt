package dev.mlds.listadecompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import dev.mlds.listadecompras.service.AuthenticateDevice.authenticateDevice
import dev.mlds.listadecompras.ui.theme.ListaDeComprasTheme
import dev.mlds.listadecompras.view.screens.CadastroItemScreen
import dev.mlds.listadecompras.view.screens.DetalheItemScreen
import dev.mlds.listadecompras.view.screens.ListaDeComprasScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticateDevice()
        setContent {
            ListaDeComprasTheme {
                val navController = rememberNavController()
                val viewModel: ListaDeComprasViewModel = viewModel()

                NavHost(navController, startDestination = "lista") {
                    composable("lista") {
                        ListaDeComprasScreen(Modifier, navController, viewModel)
                    }
                    composable("cadastro") {
                        CadastroItemScreen(navController) { name, value, userLocation ->
                            viewModel.addItem(name, value, userLocation)
                        }
                    }
                    composable(
                        "detalhe/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                        DetalheItemScreen(itemId, viewModel, navController)
                    }
                }
            }
        }
    }
}