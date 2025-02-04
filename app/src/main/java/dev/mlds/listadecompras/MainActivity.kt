package dev.mlds.listadecompras

import ListaDeComprasScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import dev.mlds.listadecompras.ui.theme.ListaDeComprasTheme
import dev.mlds.listadecompras.view.CadastroItemScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListaDeComprasTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "lista") {
                    composable("lista") {
                        ListaDeComprasScreen(modifier = Modifier, navController)
                    }
                    composable("cadastro") {
                        CadastroItemScreen(navController) { name, value ->

                        }
                    }
                }
            }
        }
    }
}