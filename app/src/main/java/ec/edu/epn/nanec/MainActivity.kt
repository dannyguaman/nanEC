package ec.edu.epn.nanec

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import ec.edu.epn.nanec.navigation.AppNavigation
import ec.edu.epn.nanec.viewmodel.AuthViewModel
import ec.edu.epn.nanec.viewmodel.EventosViewModel
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel


class MainActivity : ComponentActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val eventosViewModel: EventosViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val eventoId = intent.getStringExtra("eventoId")
            AppNavigation(navController = navController,
                          usuarioViewModel = usuarioViewModel,
                          eventosViewModel = eventosViewModel,
                          authViewModel = authViewModel
                )
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    usuarioViewModel.fcmToken = task.result
                    Log.d("FCM", "Token registrado: ${task.result}")
                } else {
                    Log.e("FCM", "Error al obtener token", task.exception)
                }
            }

            LaunchedEffect(eventoId) {
                eventoId?.let {
                    navController.navigate("detalle_evento/$it") {
                        popUpTo("lista_eventos") { inclusive = false }
                    }
                }
            }
        }
    }
}