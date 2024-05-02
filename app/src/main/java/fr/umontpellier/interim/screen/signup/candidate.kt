package fr.umontpellier.interim.screen.signup

import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
        navController.navigate(Routes.SignUp.Choice.route)
        return
    }
    var first_name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var enterprise by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    val onSubmit: () -> Unit = {
        Firebase.firestore
            .collection("user")
            .document(user.uid)
            .set(hashMapOf(
                "type" to "Candidate",
                "first_name" to first_name,
                "last_name" to last_name,
                "enterprise" to enterprise,
                "phone" to phone,
                "additional_info" to additionalInfo
            ))
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(12.dp).fillMaxSize()) {
        Text(
            "Êtes-vous un employeur ou un candidat ?",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(last_name, onValueChange = { last_name = it })
            Spacer(modifier = Modifier.width(12.dp))
            TextField(first_name, onValueChange = { first_name = it })
        }
        TextField(enterprise, onValueChange = { enterprise = it })
        Spacer(modifier = Modifier.height(12.dp))
        TextField(phone, onValueChange = { phone = it })
        Spacer(modifier = Modifier.height(12.dp))
        TextField(additionalInfo, onValueChange = { additionalInfo = it })
        Spacer(modifier = Modifier.height(36.dp))
        Button(onClick = onSubmit) {
            Text("Terminer l'inscription")
        }
    }
}