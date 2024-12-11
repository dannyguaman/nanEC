package ec.edu.epn.nanec

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import ec.edu.epn.nanec.navigation.AppNavigation
import ec.edu.epn.nanec.viewmodel.EventosViewModel
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel


class MainActivity : ComponentActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val eventosViewModel: EventosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                usuarioViewModel.fcmToken = task.result
                Log.d("FCM", "Token registrado: ${task.result}")
            } else {
                Log.e("FCM", "Error al obtener token", task.exception)
            }


        }
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController, usuarioViewModel, eventosViewModel)
        }
    }
}