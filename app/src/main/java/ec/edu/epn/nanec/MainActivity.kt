package ec.edu.epn.nanec

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import ec.edu.epn.nanec.api.RetrofitInstance
import ec.edu.epn.nanec.uin.ListaEventosScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prueba manual de la API
        /*
        lifecycleScope.launch {
            try {
                val eventosApi = RetrofitInstance.api
                val eventosRecibidos = eventosApi.obtenerEventos()
                Log.d("SALIDA", "Prueba de salida en MainActivity")
                eventosRecibidos.forEach { evento ->
                    Log.d("SALIDA", "Evento recibido: $evento")
                }
            } catch (e: Exception) {
                Log.e("SALIDA", "Error al obtener eventos", e)
            }
        }*/
        /*para obtener datos del token de registro de un cliente */
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = "Token actual: $token"
            Log.d("FCM", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        setContent{
            ListaEventosScreen()
        }

    }
}