package ec.edu.epn.nanec.uin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ec.edu.epn.nanec.model.Evento
import ec.edu.epn.nanec.viewmodel.AuthViewModel
import ec.edu.epn.nanec.viewmodel.EventosViewModel

@Composable
fun ListaEventosScreen(eventosViewModel: EventosViewModel = viewModel(),
                       navController: NavHostController,
                       authViewModel: AuthViewModel
                        ) {
    val eventos = eventosViewModel.eventos.collectAsState().value

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = {
                    authViewModel.cerrarSesion()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Cerrar sesión")
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(eventos) { evento ->
                    EventoItem(evento)
                    Log.d("EVENTO", evento.toString())
                }
            }
            Button(
                onClick = { navController.navigate("seleccion_suscripcion") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Ir a Suscripción")
            }
        }
    }

}

@Composable
fun EventoItem(evento: Evento) {
    BasicText(
        text = "Nombre: ${evento.nombre}\nUbicación: ${evento.ubicacion}\nDescripción: ${evento.descripcion}\nFecha: ${evento.fecha}",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}