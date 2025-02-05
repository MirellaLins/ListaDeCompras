package dev.mlds.listadecompras.view.componentes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.mlds.listadecompras.model.Item
import kotlin.math.roundToInt

@Composable
fun SwipeToRevealItem(
    item: Item,
    onItemChecked: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var offsetX by remember { mutableStateOf(0f) }
    val itemWidth = 200.dp // Largura do item principal
    val maxOffset = itemWidth / 2 // Limite de scroll até metade do tamanho do item
    val animatedOffset by animateFloatAsState(targetValue = offsetX)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        // Fundo que aparece ao arrastar (botões à esquerda)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.White,
                modifier = Modifier.padding(end = 16.dp)
                    .clickable { onEdit() }
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                tint = Color.White,
                modifier = Modifier
                    .clickable { onDelete() }
            )
        }

        // Conteúdo principal do item
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffset.toPx())
                    }
                },
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemChecked() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.checked,
                    onCheckedChange = { onItemChecked() }
                )
                Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    Text(text = item.name)
                    Text(text = "R$ %.2f".format(item.value))
                }
            }
        }
    }
}