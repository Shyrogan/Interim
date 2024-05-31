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
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            if (offer.logo.isNotEmpty()) {
                AsyncImage(
                    model = offer.logo,
                    contentDescription = "Logo de ${offer.name}",
                    modifier = Modifier
                        .size(250.dp)  // Increased size for prominence
                        .padding(8.dp)  // Adjusted padding for better visual spacing
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))  // Increased spacing for better section separation

        Text(
            text = offer.name,
            style = MaterialTheme.typography.h5,  // Adjusted for better emphasis and readability
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(4.dp))  // Increased spacing for better section separation

        Text(
            "Intitulé du métier ciblé: ${offer.jobTitle}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            "Profils recherchés:",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
        offer.profilesNeeded.forEach { profile ->
            Text(
                profile,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 32.dp)  // Increased indentation for visual hierarchy
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            "Période: ${formatTimestamp(offer.start)} - ${formatTimestamp(offer.end)}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            "Rémunération: ${offer.remuneration}€",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            "Description: ${offer.description}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun formatTimestamp(timestamp: Timestamp): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    return timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).format(formatter)
}
