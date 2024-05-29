package fr.umontpellier.interim.screen.signup

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
import fr.umontpellier.interim.data.User

@Composable
fun SignUpCandidate() {
    val navController = LocalNavHost.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUpChoice.route)
        return
    }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    val onSubmit: () -> Unit = {
        Firebase.firestore
            .collection("user")
            .document(user.uid)
            .set(
                User(
                    null,
                    firstName,
                    lastName,
                    "fr",
                    phone,
                )
            )
    }


    Column(
        verticalArrangement = Arrangement.Center, modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Text(
            "Inscrivez-vous dès maintenant et trouvez l'emploi de vos rêves !",

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
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom de famille") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.weight(1f)
            )

        }
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nationality,
            onValueChange = { nationality = it },
            label = { Text("Nationalité") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Téléphone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Informations supplémentaires") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(36.dp))
        Button(onClick = onSubmit) {
            Text("Terminer l'inscription")
        }
    }
}