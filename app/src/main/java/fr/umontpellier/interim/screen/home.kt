package fr.umontpellier.interim.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes

@Composable
fun Home() {
    val navHost = LocalNavHost.current
    Column {
        TextButton(onClick = {
            navHost.navigate(Routes.SignUp.route)
        }) {
            Text("S'inscrire")
        }
        TextButton(onClick = {
            navHost.navigate(Routes.SignIn.route)
        }) {
            Text("Se connecter")
        }
    }
}