package fr.umontpellier.interim.screen.offer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.component.candidate.Candidate
import fr.umontpellier.interim.component.offer.OfferDetailsComponent
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.data.User

@Composable
fun OfferPage(offerId: String) {

    var offerData by remember { mutableStateOf<Offer?>(null) }
    var user by remember { mutableStateOf<User?>(null) }


    Firebase.firestore
        .collection("offer")
        .document(offerId)
        .get()
        .addOnSuccessListener {
            offerData = it.toObject<Offer>()
        }

    Firebase.firestore
        .collection("user")
        .document(Firebase.auth.currentUser!!.uid)
        .get()
        .addOnSuccessListener {
            user = it.toObject<User>()
        }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier.weight(2f)) {
            offerData?.let { OfferDetailsComponent(it) }
        }
        if (user == null) {
            Text(text = "Loading...")
            return
        } else if (user!!.isCandidate) {
            Card(modifier = Modifier.weight(1f)) {
                Candidate(offerId = offerId)
            }
        }
    }
}