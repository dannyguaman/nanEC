package ec.edu.epn.nanec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _usuarioAutenticado = MutableStateFlow(false)
    val usuarioAutenticado: StateFlow<Boolean> = _usuarioAutenticado

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        verificarSesion()
    }

    private fun verificarSesion() {
        _usuarioAutenticado.value = firebaseAuth.currentUser != null
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        if (correo.isNullOrEmpty() || contrasena.isNullOrEmpty()) {
            _errorMessage.value = "El correo y la contraseña son requeridos"
            return
        }
        _errorMessage.value = null
        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { res ->
                    _usuarioAutenticado.value = res.isSuccessful
                    if (!res.isSuccessful) {
                        _errorMessage.value = res.exception?.message ?: "Error interno desconocido"
                    }
                }
        }
    }

    fun iniciarSesionConGoogle(googleIdToken: String?) {
        _errorMessage.value = null
        if(googleIdToken !=  null){
            val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
            viewModelScope.launch {
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { res ->
                        _usuarioAutenticado.value = res.isSuccessful
                        if (!res.isSuccessful) {
                            _errorMessage.value = res.exception?.message ?: "Error interno desconocido- GOOGLE"
                        }
                    }
            }
        }else {
            _errorMessage.value = "No se recibió un ID Token válido"
        }

    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
        _usuarioAutenticado.value = false
    }
}
