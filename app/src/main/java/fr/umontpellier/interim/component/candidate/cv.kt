package fr.umontpellier.interim.component.candidate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ShowCV(cvName: String, onUpdate: () -> Unit, onDelete: () -> Unit, onView: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = cvName,
            modifier = Modifier
                .weight(1f)
                .clickable { onView() },
            style = MaterialTheme.typography.body1
        )

        IconButton(onClick = onUpdate) {
            Icon(Icons.Default.Edit, contentDescription = "Éditer", tint = MaterialTheme.colors.primary)
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colors.error)
        }

    }
}