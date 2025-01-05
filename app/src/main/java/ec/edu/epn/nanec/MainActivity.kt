package ec.edu.epn.nanec

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.messaging.FirebaseMessaging
import ec.edu.epn.nanec.navigation.AppNavigation
import ec.edu.epn.nanec.viewmodel.AuthViewModel
import ec.edu.epn.nanec.viewmodel.EventosViewModel
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val eventosViewModel: EventosViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var clienteOneTap: SignInClient // Cliente de Google One Tap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clienteOneTap = Identity.getSignInClient(this)
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
    fun iniciarGoogleOneTap() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) //Pides el ID Token
                    .setServerClientId(getString(R.string.web_client_id)) // ID cliente web que lo obtenemos en la consola
                    .setFilterByAuthorizedAccounts(false)  //muestra todas lsa cuentas del dispositivo
                    .build()
            ).build()

        clienteOneTap.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    launcherResultadoOneTap.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )

                } catch (e: Exception){
                    Log.e("GoogleOneTap", "Error al iniciar el flujo One Tap: ${e.message}")
                }
            }
            .addOnFailureListener {
                Log.e("GoogleOneTap", "Error al iniciar One Tap: ${it.message}")
            }
    }
    private val launcherResultadoOneTap = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            try {
                val credential = clienteOneTap.getSignInCredentialFromIntent(data)
                Log.e("GoogleOneTap", "Credenciales recibidas: ${credential.id + credential.displayName}")
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    lifecycleScope.launch {
                        authViewModel.iniciarSesionConGoogle(idToken)
                    }
                } else {
                    Log.e("GoogleOneTap", "No se recibió un ID Token")
                }
            } catch (e: ApiException) {
                Log.e("GoogleOneTap", "Error al manejar One Tap: ${e.message}")
            }
        } else {
            Log.e("GoogleOneTap", "El usuario canceló el inicio de sesión")
        }
    }
}