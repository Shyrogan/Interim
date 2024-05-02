package fr.umontpellier.interim

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.umontpellier.interim.screen.Home
import fr.umontpellier.interim.screen.SignUp

sealed class Routes(val route: String) {
    data object Home: Routes("home")
    data object SignIn: Routes("sign-in")
    data object SignUp: Routes("sign-up")
}

@Composable
fun InterimApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, Routes.SignUp.route) {
        composable(Routes.Home.route) {
            Home()
        }
        composable(Routes.SignIn.route) {
            Home()
        }
        composable(Routes.SignUp.route) {
            SignUp()
        }
    }
}