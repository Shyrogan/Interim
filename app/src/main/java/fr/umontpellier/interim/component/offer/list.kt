package fr.umontpellier.interim.component.offer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.umontpellier.interim.data.Offer

@Composable
fun OfferList(offers: List<Offer.WithUser>) {
    offers.forEach { (offer, user) ->
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, Color.Gray)
                .background(Color.LightGray.copy(alpha = 0.4F))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                // TODO: Add placeholder
                AsyncImage(
                    model = offer.logo, contentDescription = "Logo de ${offer.name}", modifier = Modifier
                        .width(64.dp)
                        .height(64.dp)
                        .padding(8.dp)
                )

                Column {
                    Text(text = offer.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = offer.description, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                    Text(
                        text = "Proposé par ${user.first_name} ${user.last_name}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}