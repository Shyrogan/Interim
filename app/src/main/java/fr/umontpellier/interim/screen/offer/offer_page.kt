package fr.umontpellier.interim.screen.offer

import androidx.compose.runtime.*
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.component.offer.OfferDetailsComponent
import fr.umontpellier.interim.data.Offer

@Composable
fun OfferPage(offerId: String) {
    var offerData by remember { mutableStateOf<Offer?>(null) }

    Firebase.firestore
        .collection("offer")
        .document(offerId)
        .get()
        .addOnSuccessListener {
            offerData = it.toObject<Offer>()
        }
    offerData?.let { OfferDetailsComponent(it) }
}