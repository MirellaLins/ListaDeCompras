import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mlds.listadecompras.model.Item

@Composable
fun ItemRow(
    item: Item,
    onItemChecked: () -> Unit,
    onItemRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemChecked() }
            .animateContentSize(), // Suaviza mudanças na UI
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onItemChecked() }
        )

        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        IconButton(
            onClick = { onItemRemoved() }
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Remover item")
        }
    }
}