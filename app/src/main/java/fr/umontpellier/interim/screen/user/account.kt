package fr.umontpellier.interim.screen.user

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.EditCandidateComponent
import fr.umontpellier.interim.component.EmployerProfileScreen
import fr.umontpellier.interim.data.User

@Composable
fun Account() {
    val navHost = LocalNavHost.current
    val context = LocalContext.current


    if (Firebase.auth.currentUser == null) {
        navHost.navigate(Routes.SignIn.route)
        return
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
    } else if (data!!.isEmployer) {
        EmployerProfileScreen(data) { updatedUser ->
            updateUserInFirebase(updatedUser, context)
        }
    } else {
        EditCandidateComponent(data) { updatedUser ->
            updateUserInFirebase(updatedUser, context)
        }
    }
}

fun updateUserInFirebase(updatedUser: User, context: Context) {
    val uid = Firebase.auth.currentUser?.uid ?: return
    Firebase.firestore
        .collection("user")
        .document(uid)
        .set(updatedUser)
        .addOnSuccessListener {
            Toast.makeText(context, "Données sauvegardées", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Erreur lors de l'enregistrement: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
}
