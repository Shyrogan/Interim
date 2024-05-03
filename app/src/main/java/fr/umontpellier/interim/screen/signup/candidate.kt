package fr.umontpellier.interim.screen.signup

//import fr.umontpellier.interim.LocalNavHost
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes

@Composable
fun SignUpCandidate() {
    val navController = LocalNavHost.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUpChoice.route)
        return
    }
    var first_name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    val onSubmit: () -> Unit = {
        Firebase.firestore
            .collection("user")
            .document(user.uid)
            .set(
                hashMapOf(
                    "type" to "Candidate",
                    "first_name" to first_name,
                    "last_name" to last_name,
                    "nationality" to nationality,
                    "phone" to phone,
                    "additional_info" to additionalInfo
                )
            )
    }


    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(12.dp).fillMaxSize()) {
        Text(
            "Iscrivez-vous et accédez à nos nombreuses offres",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = last_name,
                onValueChange = { last_name = it },
                label = { Text("Nom de famille") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextField(
                value = first_name,
                onValueChange = { first_name = it },
                label = { Text("Prénom") },
                modifier = Modifier.weight(1f)
            )

        }
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nationality,
            onValueChange = { nationality = it },
            label = { Text("Nationalité") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Téléphone") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Informations supplémentaires") }
        )
        Spacer(modifier = Modifier.height(36.dp))
        Button(onClick = onSubmit) {
            Text("Terminer l'inscription")
        }
    }
}