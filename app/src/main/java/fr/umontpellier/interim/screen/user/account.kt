package fr.umontpellier.interim.screen.user

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.data.User

@Composable
fun Account() {
    val navHost = LocalNavHost.current
    if (Firebase.auth.currentUser == null) {
        navHost.navigate(Routes.SignIn.route)
    }
    var data by remember { mutableStateOf<User?>(null) }
    Firebase.firestore
        .collection("user")
        .document(Firebase.auth.currentUser!!.uid)
        .get()
        .addOnSuccessListener {
            data = it.toObject<User>()
        }

    if (data == null) {
        Text(text = "Loading...")
        return
    }
    Text(text = "$data")
}