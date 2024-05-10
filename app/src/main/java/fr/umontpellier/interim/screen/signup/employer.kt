package fr.umontpellier.interim.screen.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun SignUpEmployer() {
    val navController = LocalNavHost.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUpChoice.route)
        return
    }

    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var publicLinks by remember { mutableStateOf(mutableListOf<String>()) }
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
                hashMapOf(
                    "type" to "Employer",
                    "first_name" to firstName,
                    "last_name" to lastName,
                    "company_name" to companyName,
                    "phone" to phone,
                    "address" to address,
                    "public_links" to publicLinks
                )
            )
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(12.dp).fillMaxSize()) {
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
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Nom de l'entreprise") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Numéro de téléphone") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresse") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Liens publics:")
        Column(modifier = Modifier.padding(4.dp)) {
            publicLinks.forEach { link ->
                Text(link, modifier = Modifier.padding(4.dp))
            }
        }
        Row {
            TextField(
                value = newLink,
                onValueChange = { newLink = it },
                label = { Text("Ajouter un lien public") },
                modifier = Modifier.weight(1f)
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
