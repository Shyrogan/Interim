package fr.umontpellier.interim

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.umontpellier.interim.screen.Home
import fr.umontpellier.interim.screen.SignUp
import fr.umontpellier.interim.screen.signup.SignUpCandidate
import fr.umontpellier.interim.screen.signup.SignUpChoice

val LocalNavHost = compositionLocalOf<NavHostController> {
    error("No NavHostController found")
}

sealed class Routes(val route: String) {
    data object Home: Routes("home")
    data object SignIn: Routes("sign-in")
    data object SignUp: Routes("sign-up") {
        data object Choice: Routes("sign-up-choice")
        data object Candidate: Routes("sign-up-candidate")
        data object Employer: Routes("sign-up-employer")
    }
}

@Composable
fun InterimApp() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavHost provides navController) {
        NavHost(navController = navController, Routes.Home.route) {
            composable(Routes.Home.route) {
                Home()
            }
            composable(Routes.SignIn.route) {
                Home()
            }
            composable(Routes.SignUp.route) {
                SignUp()
            }
            composable(Routes.SignUp.Choice.route) {
                SignUpChoice()
            }
            composable(Routes.SignUp.Candidate.route) {
                SignUpCandidate()
            }
        }
    }
}