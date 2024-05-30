package fr.umontpellier.interim

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.umontpellier.interim.component.BottomNavigationBar
import fr.umontpellier.interim.screen.Home
import fr.umontpellier.interim.screen.Research
import fr.umontpellier.interim.screen.SignIn
import fr.umontpellier.interim.screen.SignUp
import fr.umontpellier.interim.screen.offer.CreateOffer
import fr.umontpellier.interim.screen.offer.EditOfferPage
import fr.umontpellier.interim.screen.offer.OfferPage
import fr.umontpellier.interim.screen.signup.SignUpCandidate
import fr.umontpellier.interim.screen.signup.SignUpChoice
import fr.umontpellier.interim.screen.signup.SignUpEmployer
import fr.umontpellier.interim.screen.user.Account
import fr.umontpellier.interim.screen.user.employer.ManageApplicationPage


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
    data object CreateOffer : Routes("create_offer")
    data object EditOffer : Routes("edit_offer/{offerId}")
    object OfferPage : Routes("offer/{offerId}")
    data object ManageApplications : Routes("manage_app/{offerId}")


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
                composable(Routes.Search.route) { Research() }
                composable(Routes.Account.route) { Account() }//Provisoire pour tester la bar

                composable(Routes.SignIn.route) { SignIn() }
                composable(Routes.SignUp.route) { SignUp() }
                composable(Routes.SignUpChoice.route) { SignUpChoice() }
                composable(Routes.SignUpCandidate.route) { SignUpCandidate() }
                composable(Routes.SignUpEmployer.route) { SignUpEmployer() }

                composable(Routes.CreateOffer.route) { CreateOffer() }
                composable(
                    route = Routes.EditOffer.route,
                    arguments = listOf(navArgument("offerId") { type = NavType.StringType })
                ) { navEntry ->
                    val offerId = navEntry.arguments?.getString("offerId")
                    if (offerId != null) {
                        EditOfferPage(offerId)
                    } else {
                        Text("Erreur : ID de l'offre non trouvé.")
                    }
                }
                composable(
                    route = Routes.OfferPage.route,
                    arguments = listOf(navArgument("offerId") { type = NavType.StringType })
                ) { navEntry ->
                    val offerId = navEntry.arguments?.getString("offerId")
                    if (offerId != null) {
                        OfferPage(offerId)
                    } else {
                        Text("Erreur : ID de l'offre non trouvé.")
                    }
                }
                composable(
                    route = Routes.ManageApplications.route,
                    arguments = listOf(navArgument("offerId") { type = NavType.StringType })
                ) { navEntry ->
                    val offerId = navEntry.arguments?.getString("offerId")
                    if (offerId != null) {
                        ManageApplicationPage(offerId)
                    } else {
                        Text("Erreur : ID de l'offre non trouvé.")
                    }
                }

            }
        }
    }
}

