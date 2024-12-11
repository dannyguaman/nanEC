package ec.edu.epn.nanec.uin

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.epn.nanec.model.Evento
import ec.edu.epn.nanec.viewmodel.EventosViewModel

@Composable
fun ListaEventosScreen(eventosViewModel: EventosViewModel = viewModel()) {
    val eventos = eventosViewModel.eventos.collectAsState().value

    MaterialTheme {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(eventos) { evento ->
                EventoItem(evento)
                Log.d("EVENTO", evento.toString())
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