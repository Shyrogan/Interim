package fr.umontpellier.interim.component.candidate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.component.offer.OfferList
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.data.User


@Composable
fun ShowCV(cvName: String, onUpdate: () -> Unit, onDelete: () -> Unit, onView: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = cvName,
            modifier = Modifier
                .weight(1f)
                .clickable { onView() },
            style = MaterialTheme.typography.body1
        )

        IconButton(onClick = onUpdate) {
            Icon(Icons.Default.Edit, contentDescription = "Éditer", tint = MaterialTheme.colors.primary)
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colors.error)
        }

    }
}

@Composable
fun ShowApplications() {
    val navController = LocalNavHost.current
    val context = LocalContext.current
    val withUserList = remember { mutableStateListOf<Offer.WithUser>() }
    val userId = Firebase.auth.currentUser?.uid

    LaunchedEffect(key1 = userId) {
        if (userId != null) {
            Firebase.firestore.collection("application")
                .whereEqualTo("candidate", Firebase.firestore.document("user/$userId"))
                .get()
                .addOnSuccessListener { applicationSnapshot ->
                    val offerRefs =
                        applicationSnapshot.documents.mapNotNull { it.getDocumentReference("offer") }.distinct()
                    offerRefs.forEach { offerRef ->
                        offerRef.get()
                            .addOnSuccessListener { offerSnapshot ->
                                val offer = offerSnapshot.toObject<Offer>()
                                if (offer != null && offer.owner != null) {
                                    val ownerRef = offer.owner as? DocumentReference
                                    ownerRef?.get()
                                        ?.addOnSuccessListener { ownerSnapshot ->
                                            val owner = ownerSnapshot.toObject<User>()
                                            if (owner != null) {
                                                withUserList.add(Offer.WithUser(offer, owner))
                                            }
                                        }
                                }
                            }
                    }
                }
        }
    }




    Column(modifier = Modifier.padding(horizontal = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        androidx.compose.material3.Text(
            text = "Vos candidature en attente:",
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        OfferList(offers = withUserList) { offerId ->
            navController.navigate("offer/$offerId")

        }
    }
}


