package fr.umontpellier.interim.component.candidate

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import fr.umontpellier.interim.LocalNavHost
import fr.umontpellier.interim.data.Application
import fr.umontpellier.interim.data.User
import fr.umontpellier.interim.data.WithUser


@Composable
fun ManageApplications(offerId: String) {
    val navController = LocalNavHost.current
    val context = LocalContext.current
    val withUserList = remember { mutableStateListOf<WithUser>() }

    LaunchedEffect(key1 = offerId) {
        Firebase.firestore.collection("application")
            .whereEqualTo("offer", Firebase.firestore.document("offer/$offerId"))
            .get()
            .addOnSuccessListener { snapshot ->
                val applications = snapshot.documents.mapNotNull { it.toObject<Application>() }
                applications.forEach { application ->
                    application.candidate?.get()
                        ?.addOnSuccessListener { userSnapshot ->
                            val user = userSnapshot.toObject<User>()
                            if (user != null) {
                                withUserList.add(WithUser(application, user))
                            }
                        }
                }
            }
    }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        item {
            Text(
                "Candidatures pour l'offre:",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(withUserList.size) { index ->
            val withUser = withUserList[index]
            ApplicationItem(
                application = withUser.application,
                user = withUser.user,
                onReject = {
                    if (withUser.application.id != null) {
                        deleteApplication(withUser.application.id, context)
                        withUserList.remove(withUser)
                    }
                },
                onContact = {
                },
                onDownloadCV = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(withUser.application.cv)))
                },
                onDownloadLetter = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(withUser.application.motivation_letter)
                        )
                    )
                }
            )
        }
    }
}


@Composable
fun ApplicationItem(
    application: Application,
    user: User,
    onReject: () -> Unit,
    onContact: () -> Unit,
    onDownloadCV: () -> Unit,
    onDownloadLetter: () -> Unit
) {
    var showContactDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    var context = LocalContext.current

    if (showContactDialog) {
        ContactDialog(user = user, onDismiss = { showContactDialog = false }, context)
    }
    if (showProfileDialog) {
        ProfileDialog(user = user, onDismiss = { showProfileDialog = false })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { showProfileDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${user.first_name} ${user.last_name}",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            if (application.cv != null && application.cv.isNotEmpty()) {
                IconButton(onClick = onDownloadCV) {
                    Icon(Icons.Filled.Description, contentDescription = "Télécharger CV")
                }
            }
            if (application.motivation_letter != null && application.motivation_letter.isNotEmpty()) {
                IconButton(onClick = onDownloadLetter) {
                    Icon(Icons.Filled.AttachFile, contentDescription = "Télécharger Lettre de Motivation")
                }
            }
            IconButton(onClick = { showContactDialog = true }) {
                Icon(Icons.Filled.Email, contentDescription = "Contacter")
            }
            IconButton(onClick = onReject) {
                Icon(Icons.Filled.Delete, contentDescription = "Rejeter", tint = MaterialTheme.colors.error)
            }
        }
    }
}

fun deleteApplication(applicationId: String, context: Context) {
    Firebase.firestore.collection("application").document(applicationId).delete()
        .addOnSuccessListener {
            Toast.makeText(context, "Candidature supprimée", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Erreur lors de la suppression: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
}


@Composable
fun ContactDialog(user: User, onDismiss: () -> Unit, context: Context) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Contact") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:${user.phone}")
                            )
                        )
                    }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Appeler")
                    }
                    Text(text = user.phone)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:${user.email}")
                            )
                        )
                    }) {
                        Icon(Icons.Filled.Email, contentDescription = "Envoyer un email")
                    }
                    Text(text = user.email)
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Fermer")
            }
        }
    )
}

@Composable
fun ProfileDialog(user: User, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profil de l'utilisateur") },
        text = {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Nom: ${user.last_name}", style = MaterialTheme.typography.body1)
                Text(text = "Prénom: ${user.first_name}", style = MaterialTheme.typography.body1)
                Text(text = "Nationalité: ${user.nationality}", style = MaterialTheme.typography.body1)
                Text(text = "Adresse: ${user.address}", style = MaterialTheme.typography.body1)
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Fermer")
            }
        }
    )
}


