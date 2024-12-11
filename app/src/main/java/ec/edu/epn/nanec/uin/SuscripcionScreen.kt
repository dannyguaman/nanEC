package ec.edu.epn.nanec.uin

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel


@Composable
fun SeleccionTipoSuscripcionScreen(
    usuarioViewModel: UsuarioViewModel,
    onSuscripcionCompletada: () -> Unit
) {
    /*permite mostrar un toast*/
    val context = LocalContext.current
    val mensajeToast = usuarioViewModel.mensajeSuscripcion.collectAsState().value
    LaunchedEffect(mensajeToast) {
        mensajeToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            usuarioViewModel.limpiarMensajeSuscrpcionToast()
        }
    }
    /*código para mostrar los botones de suscripción*/
    val tipos = listOf("musical", "gastronomico", "sitio")

    Column(modifier = Modifier.padding(16.dp)) {
        tipos.forEach { tipo ->
            Button(
                onClick = {
                    usuarioViewModel.suscribirUsuario(tipo)
                    onSuscripcionCompletada()
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Suscribirse a evento $tipo")
            }
        }
    }
}
