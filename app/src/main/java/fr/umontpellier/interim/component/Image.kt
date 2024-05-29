package fr.umontpellier.interim.component

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text

import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import java.util.*

@Composable
fun ImagePicker(onImagePicked: (Uri) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }

    TextButton(onClick = { launcher.launch("image/*") }) { Text("ajouter image") }

}


fun uploadImageToFirebaseStorage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("${Firebase.auth.currentUser?.uid}/${UUID.randomUUID()}")

    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}


@Composable
fun UpdateImagePicker(currentUrl: String, onImageUpdated: (String) -> Unit) {
    val context = LocalContext.current
    var uri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImageToFirebaseStorage(it,
                onSuccess = onImageUpdated,
                onFailure = { exception ->
                    Toast.makeText(
                        context,
                        "Erreur de téléchargement: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    Column {
        if (currentUrl.isNotEmpty()) {
            AsyncImage(
                model = currentUrl,
                contentDescription = "Logo actuel",
                modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
            )
        }
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Changer l'image")
        }
    }
}