package ec.edu.epn.nanec.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.epn.nanec.api.RetrofitInstance
import ec.edu.epn.nanec.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {
    var fcmToken: String? = null

    private val _mensajeSuscripcion = MutableStateFlow<String?>(null)
    val mensajeSuscripcion: StateFlow<String?> = _mensajeSuscripcion

    private val _ubicacion = MutableStateFlow<Pair<Double, Double>?>(null)
    val ubicacion: StateFlow<Pair<Double, Double>?> = _ubicacion

    fun suscribirUsuario(tipoSuscripcion: String) {
        if (tipoSuscripcion !in listOf("musical", "gastronomico", "sitio")) {
            Log.e("SUSCRIPCION", "Tipo de suscripción inválido: $tipoSuscripcion")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val usuario = Usuario(fcmToken ?: "", tipoSuscripcion)
                RetrofitInstance.api.registrarUsuario(usuario)
                Log.d("SUSCRIPCION", "Usuario registrado con éxito: $tipoSuscripcion")
                _mensajeSuscripcion.value = "Suscripción exitosa"
            } catch (e: Exception) {
                Log.e("SUSCRIPCION", "Error al registrar usuario", e)
                _mensajeSuscripcion.value = "Error al suscribirse en esta categoría"
            }
        }
    }
    fun limpiarMensajeSuscrpcionToast(){
        _mensajeSuscripcion.value = null
    }

    fun actualizarUbicacion(latitud: Double, longitud: Double) {
        _ubicacion.value = Pair(latitud, longitud)
    }
}
