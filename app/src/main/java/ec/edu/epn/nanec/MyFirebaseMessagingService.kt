package ec.edu.epn.nanec

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Mensaje recibido de: ${remoteMessage.from}")
        val eventoId = remoteMessage.data["eventoId"]

        if (eventoId != null) {
            Log.d("FCM", "Evento recibido con ID: $eventoId")
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("eventoId", eventoId)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Token actualizado: $token")
        // Esto servirá para emular el servidor backend
    }
}
