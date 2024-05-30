package fr.umontpellier.interim.screen.signup

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun SignUpEmployer() {
    val navController = LocalNavHost.current
    val context = LocalContext.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUpChoice.route)
        return
    }

    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyPosition by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val publicLinks by remember { mutableStateOf(mutableListOf<String>()) }
    var newLink by remember { mutableStateOf("") }

    val addLink = {
        if (newLink.isNotEmpty()) {
            publicLinks.add(newLink)
            newLink = ""
        }
    }

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
                    companyName,
                    companyPosition,
                    address,
                    publicLinks,
                    ""
                )
            )
            .addOnSuccessListener {
                navController.navigate(Routes.Account.route)
                Toast.makeText(context, "Enregistrement réussi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erreur lors de l'enregistrement: ${e.localizedMessage}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    Column(
        verticalArrangement = Arrangement.Center, modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Text(
            "Rejoignez-nous pour recruter les talents qui propulseront votre entreprise !",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom de famille") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Nom de l'entreprise") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = companyPosition,
            onValueChange = { companyPosition = it },
            label = { Text("Poste dans l'entreprise") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Numéro de téléphone") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresse") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Liens publics:")
        Column(modifier = Modifier.padding(4.dp)) {
            publicLinks.forEach { link ->
                Text(link, modifier = Modifier.padding(4.dp))
            }
        }
        Row {
            OutlinedTextField(
                value = newLink,
                onValueChange = { newLink = it },
                label = { Text("Ajouter un lien public") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            IconButton(onClick = addLink) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter un lien")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSubmit) {
            Text("Suivant")
        }
    }
}
