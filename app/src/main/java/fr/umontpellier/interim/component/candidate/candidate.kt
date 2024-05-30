package fr.umontpellier.interim.component.candidate

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.Routes
import fr.umontpellier.interim.component.uploadFileToFirebaseStorage
import fr.umontpellier.interim.data.Application

@Composable
fun Candidate(offerId: String) {
    val context = LocalContext.current
    val navController = LocalNavHost.current
    var documentUri by remember { mutableStateOf<Uri?>(null) }
    var documentUrl by remember { mutableStateOf("") }
    var documentName by remember { mutableStateOf("Joindre une lettre de motivation") }

    var cvName by remember { mutableStateOf("Joindre mon cv") }
    var cvRef by remember { mutableStateOf("") }
    var cvUploaded by remember { mutableStateOf(false) }

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            documentUri = uri
            documentName =
                uri?.let { it.lastPathSegment ?: "Lettre de motivation ajoutée" } ?: "Fichier non sélectionné"
            uri?.let {
                uploadFileToFirebaseStorage(it,
                    onSuccess = { url ->
                        documentUrl = url
                        Toast.makeText(context, "Lettre de motivation chargée avec succès: $url", Toast.LENGTH_LONG)
                            .show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            context,
                            "Erreur lors du chargement: ${exception.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    )
    val onSubmit: () -> Unit = {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val userRef = Firebase.firestore.collection("user").document(currentUser.uid)
            val offerRef = Firebase.firestore.collection("offer").document(offerId)

            fun submitApplication() {
                Firebase.firestore
                    .collection("application")
                    .add(
                        Application(
                            null,
                            offerRef,
                            userRef,
                            cv = cvRef,
                            motivation_letter = documentUrl,
                        )
                    )
                    .addOnSuccessListener {
                        navController.navigate(Routes.Search.route)
                        Toast.makeText(context, "Candidature envoyée", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Erreur lors de l'envoi de la candidature: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

            if (cvUploaded) {
                val userDocRef = Firebase.firestore.collection("user").document(currentUser.uid)
                userDocRef.get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val cvUrl = document.getString("cv")
                        if (cvUrl != null && cvUrl.isNotEmpty()) {
                            cvRef = cvUrl
                        }
                    }
                    submitApplication()
                }.addOnFailureListener {
                    navController.navigate(Routes.Account.route)
                    Toast.makeText(context, "Verifiez d'abord votre CV", Toast.LENGTH_SHORT).show()
                }

            } else {
                submitApplication()
            }
        }
    }


    Column(modifier = Modifier.padding(start = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = { navController.navigate(Routes.Account.route) })
        ) {
            Icon(Icons.Outlined.AccountCircle, contentDescription = "Vérifier mes informations")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Vérifier mes informations")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = {
                cvName = "Mon_CV.pdf"
                cvUploaded = true
            })
        ) {
            Icon(Icons.Outlined.Description, contentDescription = "Joindre mon CV")
            Spacer(modifier = Modifier.width(12.dp))
            Text(cvName)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = { documentLauncher.launch("application/pdf") })
        ) {
            Icon(Icons.Filled.AttachFile, contentDescription = "Joindre une lettre de motivation")
            Spacer(modifier = Modifier.width(12.dp))
            Text(documentName)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = onSubmit) {
                Text("Candidater")
            }
        }
    }
}