package fr.umontpellier.interim.screen.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes

@Composable
fun SignUpChoice() {
    val navController = LocalNavHost.current
    val user = Firebase.auth.currentUser
    if (user == null) {
        navController.navigate(Routes.SignUp.Choice.route)
        return
    }
    val onChoice: (Int) -> () -> Unit = { id -> {
        navController.navigate(if (id == 0) Routes.SignUp.Employer.route else Routes.SignUp.route)
    } }

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