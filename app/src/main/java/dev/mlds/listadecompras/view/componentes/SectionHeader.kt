package dev.mlds.listadecompras.view.componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import dev.mlds.listadecompras.model.Item

@Composable
fun SectionHeader(
    title: String,
    items: List<Item>,
    onToggleAll: (Boolean) -> Unit
) {
    val allChecked = items.all { it.checked }
    val noneChecked = items.none { it.checked }
    val checkboxState = when {
        allChecked -> ToggleableState.On
        noneChecked -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        TriStateCheckbox(
            state = checkboxState,
            onClick = { onToggleAll(!allChecked) }
        )
    }
}
