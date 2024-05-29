package fr.umontpellier.interim.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.component.offer.OfferList
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.data.User

@Preview
@Composable
fun Research() {
    val offers = remember {
        mutableStateListOf<Offer.WithUser>()
    }
    var value by remember {
        mutableStateOf("")
    }
    val dummy = rememberSaveable { true }
    val onResearchChange: (String) -> Unit = {
        value = it
        offers.clear()
        Firebase.firestore
            .collection("offer")
            .limit(3)
            .orderBy("name")
            .startAt(it)
            .endAt("$it\uf8ff")
            .get()
            .addOnSuccessListener { offerSnapshot ->
                offers.clear()
                offerSnapshot.documents
                    .mapNotNull { it.toObject<Offer>() }
                    .forEach { offer ->
                        offer.owner.get()
                            .addOnSuccessListener { userSnapshot ->
                                val user = userSnapshot.toObject<User>()
                                if (user != null && !offers.contains(Offer.WithUser(offer, user))) {
                                    offers.add(Offer.WithUser(offer, user))
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Research", "Error loading user data", exception)
                            }
                            .addOnCanceledListener {
                                Log.e("Research", "User data load canceled")
                            }
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("Research", "Error loading offers", exception)
            }
    }
    // Au début, on exécute une recherche à vide...
    LaunchedEffect(dummy) {
        onResearchChange("")
    }
    Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        TextField(value, onValueChange = onResearchChange, modifier = Modifier.fillMaxWidth())
        Text(
            text = "Nos offres pour votre recherche:",
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        OfferList(offers)
    }
}