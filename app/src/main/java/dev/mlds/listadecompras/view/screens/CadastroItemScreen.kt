package dev.mlds.listadecompras.view.screens

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun CadastroItemScreen(
    navController: NavController,
    onItemAdded: (String, Double, String?) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemValue by remember { mutableStateOf("") }
    var locationEnabled by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                fetchLocation(context, fusedLocationClient) { location ->
                    userLocation = location
                }
            } else {
                Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
                locationEnabled = false
            }
        }
    )

    fun checkAndRequestPermission() {
        val hasPermission = PermissionChecker.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        if (hasPermission) {
            fetchLocation(context, fusedLocationClient) { location ->
                userLocation = location
            }
        } else {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Usar Localização")
            Switch(
                checked = locationEnabled,
                onCheckedChange = { isChecked ->
                    locationEnabled = isChecked
                    if (isChecked) checkAndRequestPermission() else userLocation = null
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        userLocation?.let {
            Text("Localização: $it", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val value = itemValue.toDoubleOrNull() ?: 0.0
                if (itemName.isNotBlank() && value > 0) {
                    onItemAdded(itemName, value, userLocation)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }
    }
}

@SuppressLint("MissingPermission")
fun fetchLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (String?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            val lat = location.latitude
            val lon = location.longitude
            onLocationReceived("Lat: $lat, Lon: $lon")
        } else {
            Toast.makeText(context, "Não foi possível obter a localização", Toast.LENGTH_SHORT).show()
            onLocationReceived(null)
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Erro ao obter localização", Toast.LENGTH_SHORT).show()
        onLocationReceived(null)
    }
}
