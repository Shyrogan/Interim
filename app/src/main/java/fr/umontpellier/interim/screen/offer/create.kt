package fr.umontpellier.interim.screen.offer

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.ImagePicker
import fr.umontpellier.interim.component.uploadImageToFirebaseStorage
import fr.umontpellier.interim.data.Offer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateOffer() {
    val navController = LocalNavHost.current
    val context = LocalContext.current


    var name by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var profilesNeeded by remember { mutableStateOf(mutableListOf<String>()) }
    var remuneration by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var start by remember { mutableStateOf(Timestamp.now()) }
    var end by remember { mutableStateOf(Timestamp.now()) }
    var newProfile by remember { mutableStateOf("") }

    var logoUri by remember { mutableStateOf<Uri?>(null) }
    var logoUrl by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()


    val addProfile = {
        if (newProfile.isNotEmpty()) {
            profilesNeeded.add(newProfile)
            newProfile = ""
        }
    }

    val onSubmit: () -> Unit = {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val userRef = Firebase.firestore.collection("user").document(currentUser.uid)
            Firebase.firestore
                .collection("offer")
                .add(
                    Offer(
                        null,
                        name,
                        jobTitle,
                        profilesNeeded,
                        remuneration,
                        description,
                        logoUrl,
                        start,
                        end,
                        userRef
                    )
                )
                .addOnSuccessListener {
                    navController.navigate(Routes.Home.route)
                    Toast.makeText(context, "Offre créée", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Erreur lors de la creation de l'offre: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        ImagePicker { uri ->
            logoUri = uri
            uploadImageToFirebaseStorage(uri,
                onSuccess = { url ->
                    logoUrl = url
                    Toast.makeText(context, "Image téléchargée avec succès", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Toast.makeText(
                        context,
                        "Erreur de téléchargement: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
        if (logoUrl.isNotEmpty()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = "Logo téléchargé",
                modifier = Modifier.size(100.dp),
            )
        }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom de l'offre") }
        )
        TextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Métier ciblé") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        androidx.compose.material3.Text("Profils recherchés")
        Column(modifier = Modifier.padding(4.dp)) {
            profilesNeeded.forEach { profile ->
                androidx.compose.material3.Text(profile, modifier = Modifier.padding(4.dp))
            }
        }
        Row {
            androidx.compose.material3.TextField(
                value = newProfile,
                onValueChange = { newProfile = it },
                label = { androidx.compose.material3.Text("Ajouter profile") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            IconButton(onClick = addProfile) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter un profile")
            }
        }
        TextField(
            value = remuneration,
            onValueChange = { remuneration = it },
            label = { Text("Rémunération") }
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        DatePicker("Date de début", start) { newStart -> start = newStart }
        DatePicker("Date de fin", end) { newEnd -> end = newEnd }

        Spacer(modifier = Modifier.height(48.dp))
        androidx.compose.material3.TextButton(onClick = onSubmit) {
            androidx.compose.material3.Text("Creer l'offre")
        }
    }
}


@Composable
fun DatePicker(label: String, timestamp: Timestamp, onDateSelected: (Timestamp) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var dateString by remember { mutableStateOf(simpleDateFormat.format(calendar.time)) }

    Button(onClick = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(year, month, dayOfMonth)
                val newTimestamp =
                    Timestamp(newCalendar.timeInMillis / 1000, (newCalendar.timeInMillis % 1000).toInt() * 1000000)
                onDateSelected(newTimestamp)
                dateString = simpleDateFormat.format(newCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }) {
        Text(text = "$label: $dateString")
    }
}
