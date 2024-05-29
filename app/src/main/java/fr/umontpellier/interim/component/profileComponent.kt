package fr.umontpellier.interim.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.umontpellier.interim.component.offer.ManageOffer
import fr.umontpellier.interim.data.User


@Composable
fun EmployerProfileScreen(user: User?, onSave: (User) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier.weight(2f)) {
            EditEmployeeComponent(user, onSave)
        }
        Card(modifier = Modifier.weight(1f)) {
            ManageOffer()
        }
    }
}

@Composable
fun EditEmployeeComponent(user: User?, onSave: (User) -> Unit) {
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
        }) {
            Text("Sauvegarder")
        }
    }

}

@Composable
fun EditCandidateComponent(user: User?, onSave: (User) -> Unit) {
    if (user == null) {
        Text("Aucune donnée disponible")
        return
    }
    var lastName by remember { mutableStateOf(user.last_name) }
    var firstName by remember { mutableStateOf(user.first_name) }
    var nationality by remember { mutableStateOf(user.nationality) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(user.address) }


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

        Button(onClick = {
            onSave(
                User(
                    first_name = firstName,
                    last_name = lastName,
                    nationality = nationality,
                    phone = phone,
                    address = address,
                )
            )
        }) {
            Text("Sauvegarder")
        }
    }

}


@Composable
fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

}
