package ec.edu.epn.nanec.uin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ec.edu.epn.nanec.viewmodel.EventosViewModel

@Composable
fun DetalleEventoScreen(eventoId: String?, eventosViewModel: EventosViewModel) {
    val eventos = eventosViewModel.eventos.collectAsState().value
    val evento = eventos.find { it._id == eventoId }

    if (evento != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${evento.nombre}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Ubicación: ${evento.ubicacion}")
            Text(text = "Descripción: ${evento.descripcion}")
            Text(text = "Fecha: ${evento.fecha}")
        }
    } else {
        Text(text = "Evento no encontrado", modifier = Modifier.padding(16.dp))
    }
}
