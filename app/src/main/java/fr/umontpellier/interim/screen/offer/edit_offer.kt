package fr.umontpellier.interim.screen.offer

import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.offer.EditOfferComponent
import fr.umontpellier.interim.data.Offer

@Composable
fun EditOfferPage(offerId: String) {
    val context = LocalContext.current
    val offer = remember { mutableStateOf<Offer?>(null) }
    val navController = LocalNavHost.current

    LaunchedEffect(offerId) {
        Firebase.firestore.collection("offer").document(offerId).get()
            .addOnSuccessListener { document ->
                offer.value = document.toObject<Offer>()
            }
    }

    offer.value?.let {
        EditOfferComponent(it) { updatedOffer ->
            Firebase.firestore.collection("offer").document(offerId).set(updatedOffer)
                .addOnSuccessListener {
                    navController.navigate(Routes.Account.route)
                    Toast.makeText(context, "Offre modifiée", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Erreur lors de modification: ${e.localizedMessage}", Toast.LENGTH_LONG)
                        .show()
                }
        }
    } ?: Text("Chargement de l'offre...")
}
