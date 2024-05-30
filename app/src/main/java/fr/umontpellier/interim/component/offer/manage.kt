package fr.umontpellier.interim.component.offer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.data.Offer

@Composable
fun ManageOffer() {
    val navController = LocalNavHost.current
    val context = LocalContext.current
    val offers = remember { mutableStateListOf<Offer>() }
    val userId = Firebase.auth.currentUser?.uid

    LaunchedEffect(key1 = userId) {
        if (userId != null) {
            Firebase.firestore.collection("offer")
                .whereEqualTo("owner", Firebase.firestore.document("user/$userId"))
                .get()
                .addOnSuccessListener { snapshot ->
                    offers.clear()
                    offers.addAll(snapshot.documents.mapNotNull { it.toObject<Offer>() })
                }
        }
    }

    LazyColumn {
        item {
            // Header
            Text("Mes offres:", style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp))
        }
        items(offers) { offer ->
            OfferItem(offer,
                onEdit = {
                    navController.navigate("edit_offer/${offer.id}")
                }, onDelete = {
                    if (offer.id != null) {
                        deleteOffer(offer.id, context)
                    }
                }, onViewResponses = {
                    navController.navigate("manage_app/${offer.id}")
                })
        }
    }
}


@Composable
fun OfferItem(offer: Offer, onEdit: () -> Unit, onDelete: () -> Unit, onViewResponses: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = offer.logo,
                contentDescription = "Logo de l'offre",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = offer.name,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Éditer", tint = MaterialTheme.colors.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colors.error)
            }
            IconButton(onClick = onViewResponses) {
                Icon(Icons.Default.Email, contentDescription = "Voir Réponses", tint = MaterialTheme.colors.primary)
            }
        }
    }
}

fun deleteOffer(offerId: String, context: Context) {
    Firebase.firestore.collection("offer").document(offerId).delete()
        .addOnSuccessListener {
            Toast.makeText(context, "Offre supprimée", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Erreur lors de la supression: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
}
