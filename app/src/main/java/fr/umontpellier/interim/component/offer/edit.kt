package fr.umontpellier.interim.component.offer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.umontpellier.interim.component.UpdateImagePicker
import fr.umontpellier.interim.data.Offer
import fr.umontpellier.interim.screen.offer.DatePicker
import androidx.compose.material.MaterialTheme as MaterialTheme1

@Composable
fun EditOfferComponent(offer: Offer, onSave: (Offer) -> Unit) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(offer.name) }
    var jobTitle by remember { mutableStateOf(offer.jobTitle) }
    val profilesNeeded = remember { mutableStateListOf<String>().also { it.addAll(offer.profilesNeeded) } }
    var remuneration by remember { mutableStateOf(offer.remuneration) }
    var description by remember { mutableStateOf(offer.description) }
    var logoUrl by remember { mutableStateOf(offer.logo) }
    var start by remember { mutableStateOf(offer.start) }
    var end by remember { mutableStateOf(offer.end) }

    var newProfile by remember { mutableStateOf("") }

    val addProfile = {
        if (newProfile.isNotEmpty()) {
            profilesNeeded.add(newProfile)
            newProfile = ""
        }
    }

    val removeProfile = { profile: String ->
        profilesNeeded.remove(profile)
        Unit
    }

    val updateOffer = {
        onSave(
            Offer(
                id = offer.id,
                name = name,
                jobTitle = jobTitle,
                profilesNeeded = profilesNeeded.toList(),
                remuneration = remuneration,
                description = description,
                logo = logoUrl,
                start = start,
                end = end,
                owner = offer.owner
            )
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        UpdateImagePicker(logoUrl) { newLogoUrl -> logoUrl = newLogoUrl }
        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom de l'offre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Métier ciblé") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ProfileListEditor(
            "Profils recherchés",
            profilesNeeded,
            newProfile,
            onProfileAdded = addProfile,
            onProfileChanged = { newProfile = it },
            onProfileRemove = removeProfile
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = remuneration,
            onValueChange = { remuneration = it },
            label = { Text("Rémunération") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker("Date de début", start) { newStart -> start = newStart }
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker("Date de fin", end) { newEnd -> end = newEnd }
        Spacer(modifier = Modifier.height(8.dp))


        Button(onClick = updateOffer) {
            Text("Sauvegarder")
        }
    }
}

@Composable
fun ProfileListEditor(
    label: String,
    profiles: MutableList<String>,
    newProfile: String,
    onProfileAdded: () -> Unit,
    onProfileChanged: (String) -> Unit,
    onProfileRemove: (String) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme1.typography.h6)
        profiles.forEach { profile ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(profile, modifier = Modifier.weight(1f))
                IconButton(onClick = { onProfileRemove(profile) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Profile")
                }
            }
        }
        OutlinedTextField(
            value = newProfile,
            onValueChange = onProfileChanged,
            label = { Text("Ajouter un profil") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onProfileAdded) {
            Text("Ajouter")
        }
    }
}
