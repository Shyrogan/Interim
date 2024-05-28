package fr.umontpellier.interim.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firestore.v1.Document
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.offer.OfferList
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.data.User

@Composable
fun Home() {
    val navHost = LocalNavHost.current

    Column {
        TextButton(onClick = {
            navHost.navigate(Routes.SignUp.route)
        }) {
            Text("S'inscrire")
        }
        TextButton(onClick = {
            navHost.navigate(Routes.SignIn.route)
        }) {
            Text("Se connecter")
        }
        BestOffers()
    }
}

@Composable
fun BestOffers() {
    val closestOffers = remember { mutableStateListOf<Offer.WithUser>() }
    val dummy = rememberSaveable { true }

    LaunchedEffect(dummy) {
        Firebase.firestore
            .collection("offer")
            .limit(3)
            .get()
            .addOnSuccessListener { offerSnapshot ->
                offerSnapshot.documents
                    .mapNotNull { it.toObject<Offer>() }
                    .forEach { offer ->
                        offer.owner.get()
                            .addOnSuccessListener { userSnapshot ->
                                val user = userSnapshot.toObject<User>()
                                if (user != null) {
                                    closestOffers.add(Offer.WithUser(offer, user))
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("BestOffers", "Error loading user data", exception)
                            }
                            .addOnCanceledListener {
                                Log.e("BestOffers", "User data load canceled")
                            }
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("BestOffers", "Error loading offers", exception)
            }
    }

    OfferList(offers = closestOffers)
}
