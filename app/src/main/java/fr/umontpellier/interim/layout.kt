package fr.umontpellier.interim

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.umontpellier.interim.component.BottomNavigationBar
import fr.umontpellier.interim.screen.Home
import fr.umontpellier.interim.screen.SignIn
import fr.umontpellier.interim.screen.SignUp
import fr.umontpellier.interim.screen.signup.SignUpCandidate
import fr.umontpellier.interim.screen.signup.SignUpChoice
import fr.umontpellier.interim.screen.signup.SignUpEmployer
import fr.umontpellier.interim.screen.user.Account


val LocalNavHost = compositionLocalOf<NavHostController> {
    error("No NavHostController found")
}


sealed class Routes(val route: String, val icon: ImageVector? = null) {
    data object Home : Routes("home", Icons.Filled.Home)
    data object Search : Routes("search", Icons.Filled.Search)
    data object Account : Routes("account", Icons.Filled.AccountCircle)
    data object SignIn : Routes("sign-in")
    data object SignUp : Routes("sign-up")
    data object SignUpChoice : Routes("sign-up-choice")
    data object SignUpCandidate : Routes("sign-up-candidate")
    data object SignUpEmployer : Routes("sign-up-employer")
}


@Composable
fun InterimApp() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavHost provides navController) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Routes.Home.route) { Home() }
                composable(Routes.Search.route) { SignUpChoice() }//Provisoire pour tester la bar
                composable(Routes.Account.route) { Account() }//Provisoire pour tester la bar

                composable(Routes.SignIn.route) { SignIn() }
                composable(Routes.SignUp.route) { SignUp() }
                composable(Routes.SignUpChoice.route) { SignUpChoice() }
                composable(Routes.SignUpCandidate.route) { SignUpCandidate() }
                composable(Routes.SignUpEmployer.route) { SignUpEmployer() }

            }
        }
    }
}