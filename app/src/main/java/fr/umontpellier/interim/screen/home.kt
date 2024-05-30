package fr.umontpellier.interim.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.R
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.offer.OfferList
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.data.User

@Composable
fun Home() {
    val navHost = LocalNavHost.current
    var isEmployer by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Interim", fontWeight = FontWeight.Bold, fontSize = 21.sp)
        Image(
            painter = painterResource(id = R.drawable.app),
            contentDescription = "Logo",
            modifier = Modifier.height(128.dp)
        )

        Row {
            if (Firebase.auth.currentUser == null) {
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
            } else {
                Firebase.firestore
                    .collection("user")
                    .document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {
                        isEmployer =  it.toObject<User>()?.isEmployer ?: false
                    }
                if (isEmployer) {
                    TextButton(onClick = {
                        navHost.navigate(Routes.CreateOffer.route)
                    }) {
                        Text("Creer une offre")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        BestOffers(navHost)
    }
}

@Composable
fun BestOffers(navHost: NavHostController) {
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

    Column(modifier = Modifier.padding(horizontal = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Nos offres qui vous correspondent:",
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        OfferList(offers = closestOffers) { offerId ->
            navHost.navigate("offer/$offerId")
        }
    }
}
