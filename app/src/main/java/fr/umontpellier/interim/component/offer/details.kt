package fr.umontpellier.interim.component.offer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import fr.umontpellier.interim.data.Offer
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OfferDetailsComponent(offer: Offer) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (offer.logo.isNotEmpty()) {
                AsyncImage(
                    model = offer.logo, contentDescription = "Logo de ${offer.name}", modifier = Modifier
                        .width(64.dp)
                        .height(64.dp)
                        .padding(8.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = offer.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Intitulé du métier ciblé: ${offer.jobTitle}",
            style = MaterialTheme.typography.subtitle1
        )

        Text("Profils recherchés:", style = MaterialTheme.typography.subtitle1)
        offer.profilesNeeded.forEach { profile ->
            Text(
                profile,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Text(
            "Période: ${formatTimestamp(offer.start)} - ${formatTimestamp(offer.end)}",
            style = MaterialTheme.typography.body1
        )
        Text(
            "Rémunération: ${offer.remuneration}€",
            style = MaterialTheme.typography.body1
        )
        Text(
            "Description: ${offer.description}",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun formatTimestamp(timestamp: Timestamp): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    return timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).format(formatter)
}
