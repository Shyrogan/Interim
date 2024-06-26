package fr.umontpellier.interim.component

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.candidate.ShowApplications
import fr.umontpellier.interim.component.candidate.ShowCV
import fr.umontpellier.interim.component.offer.ManageOffer
import fr.umontpellier.interim.data.User


@Composable
fun EmployerProfileScreen(user: User?, onSave: (User) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {
            EditEmployeeComponent(user, onSave)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ManageOffer()
        }
    }
}

@Composable
fun CandidateProfileScreen(user: User?, onSave: (User) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {
            EditCandidateComponent(user, onSave)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ShowApplications()
        }
    }
}

@Composable
fun EditEmployeeComponent(user: User?, onSave: (User) -> Unit) {
    var navHost = LocalNavHost.current
    if (user == null) {
        Text("Aucune donnée disponible")
        return
    }
    var lastName by remember { mutableStateOf(user.last_name) }
    var firstName by remember { mutableStateOf(user.first_name) }
    var companyName by remember { mutableStateOf(user.company) }
    var companyPosition by remember { mutableStateOf(user.company_position) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(user.address) }


    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "${user.first_name} ${user.last_name} | ${user.company_position} au sein de ${user.company}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        EditableProfileField("Nom de famille", lastName) { lastName = it }
        EditableProfileField("Prénom", firstName) { firstName = it }
        EditableProfileField("Nom de l'entreprise", companyName) { companyName = it }
        EditableProfileField("Poste dans l'entreprise", companyPosition) { companyPosition = it }
        EditableProfileField("Numéro de téléphone", phone) { phone = it }
        EditableProfileField("Adresse", address) { address = it }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                onSave(
                    User(
                        first_name = firstName,
                        last_name = lastName,
                        company = companyName,
                        company_position = companyPosition,
                        phone = phone,
                        address = address,
                    )
                )
                navHost.navigate(Routes.Account.route)

            }) {
                Text("Sauvegarder")
            }

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navHost.navigate(Routes.Home.route)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)
            ) {
                androidx.compose.material3.Text(text = "Se déconnecter")
            }
        }


    }
}

@Composable
fun EditCandidateComponent(user: User?, onSave: (User) -> Unit) {
    var context = LocalContext.current
    var navHost = LocalNavHost.current
    if (user == null) {
        Text("Aucune donnée disponible")
        return
    }
    var email = user.email
    var lastName by remember { mutableStateOf(user.last_name) }
    var firstName by remember { mutableStateOf(user.first_name) }
    var nationality by remember { mutableStateOf(user.nationality) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(user.address) }
    var cvUrl by remember { mutableStateOf(user.cv) }

    val cvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                uploadFileToFirebaseStorage(it,
                    onSuccess = { url ->
                        cvUrl = url
                        Toast.makeText(context, "CV téléchargé avec succès: $url", Toast.LENGTH_LONG).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            context,
                            "Erreur lors du téléchargement: ${exception.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    )


    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Bienvenu sur votre espace ${user.first_name}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        EditableProfileField("Nom de famille", lastName) { lastName = it }
        EditableProfileField("Prénom", firstName) { firstName = it }
        EditableProfileField("Nationalite", nationality) { nationality = it }
        EditableProfileField("Numéro de téléphone", phone) { phone = it }
        EditableProfileField("Adresse", address) { address = it }


        if (cvUrl.isNotEmpty()) {
            ShowCV(
                cvName = "Mon CV.pdf",
                onUpdate = { cvLauncher.launch("application/pdf") },
                onDelete = {
                    cvUrl = ""
                },
                onView = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(cvUrl)))
                }
            )
        } else {
            Button(
                onClick = { cvLauncher.launch("application/pdf") },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Ajouter CV")
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {


            Button(onClick = {
                onSave(
                    User(
                        first_name = firstName,
                        last_name = lastName,
                        email = email,
                        nationality = nationality,
                        phone = phone,
                        address = address,
                        cv = cvUrl
                    )
                )
                navHost.navigate(Routes.Account.route)

            }) {
                Text("Sauvegarder")
            }
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navHost.navigate(Routes.Home.route)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)
            ) {
                androidx.compose.material3.Text(text = "Se déconnecter")
            }
        }
    }

}


@Composable
fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
}
