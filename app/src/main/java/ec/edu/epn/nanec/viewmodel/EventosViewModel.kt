package ec.edu.epn.nanec.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.epn.nanec.api.RetrofitInstance
import ec.edu.epn.nanec.model.Evento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventosViewModel : ViewModel() {
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    init {
        //Log.d("ViewModel", "ViewModel inicializado")
        cargarEventos()
    }

    private fun cargarEventos() {
        viewModelScope.launch {
            try {
                val eventosApi = RetrofitInstance.api
                val eventosRecibidos = eventosApi.obtenerEventos()
                _eventos.value = eventosRecibidos
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error desconocido: ${e.message}")
            }
        }
    }
}