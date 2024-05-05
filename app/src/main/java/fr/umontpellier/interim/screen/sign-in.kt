package fr.umontpellier.interim.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes

@Preview
@Composable
fun SignIn() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val navHost = LocalNavHost.current

    val onSubmit: () -> Unit = {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCanceledListener {
                Toast.makeText(context, "Erreur de connexion !", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "${e.message}.", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(context, "Vous voilà connecté !", Toast.LENGTH_SHORT).show()
            }
    }

    val navToSignUpPage: () -> Unit = {
        navHost.navigate(Routes.SignUp.route)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .fillMaxSize()
    ) {
        Text(
            "Connectez-vous et rejoignez une communauté de chômageur partiels.",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adresse e-mail") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(48.dp))
        TextButton(onClick = onSubmit) {
            Text("Connexion")
        }


        Text("Pas encore de compte?", textAlign = TextAlign.Center, fontWeight = FontWeight.Light, fontSize = 10.sp)

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = navToSignUpPage) {
            Text("S'inscrire ")
        }
    }
}
