package fr.umontpellier.interim.screen.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes

@Composable
fun SignUpChoice() {
    val navController = LocalNavHost.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUpChoice.route)
        return
    }
    val onChoice: (Int) -> () -> Unit = { id ->
        {
            navController.navigate(if (id == 0) Routes.SignUpEmployer.route else Routes.SignUpCandidate.route)
        }
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
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
            Button(onClick = onChoice(0)) {
                Text("Employeur")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = onChoice(1)) {
                Text("Candidat")
            }
        }
    }
}