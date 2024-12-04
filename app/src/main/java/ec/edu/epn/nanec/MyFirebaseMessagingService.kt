package ec.edu.epn.nanec

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Mensaje recibido de: ${remoteMessage.from}")

        // Procesar mensajes de datos
        remoteMessage.data?.let {
            Log.d("FCM", "Datos: $it")
            // Aquí actualizaremos LiveData
            val nombre = it["nombre"] ?: "Desconocido"
            val direccion = it["direccion"] ?: "Desconocido"
            val descripcion = it["descripcion"] ?: "Sin descripción"

            // Guardar el punto de interés en SQLite
            Log.d("FCM", "Datos: $nombre, $direccion, $descripcion")
        }

        // Procesar mensajes de notificación
        remoteMessage.notification?.let {
            Log.d("FCM", "Notificación: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Token actualizado: $token")
        // Esto servirá para emular el servidor backend
    }
}
