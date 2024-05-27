package fr.umontpellier.interim.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firestore.v1.Document
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.data.Offer

@Composable
fun Home() {
    val navHost = LocalNavHost.current
    val closestOffers = remember {
        mutableStateListOf<Offer>()
    }

    Firebase.firestore
        .collection("offers")
        .limit(3)
        .get()
        .addOnSuccessListener {
            closestOffers.addAll(
                it.documents.mapNotNull { d -> d.toObject<Offer>() }
            )
        }

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
    }
}