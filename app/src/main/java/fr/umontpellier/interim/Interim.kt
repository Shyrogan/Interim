package fr.umontpellier.interim

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

/**
 * [Application] implementation pour [Interim].
 */
class Interim: Application()

class InterimActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InterimApp()
        }
    }
}