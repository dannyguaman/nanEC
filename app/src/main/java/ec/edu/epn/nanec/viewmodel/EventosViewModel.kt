package ec.edu.epn.nanec.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.epn.nanec.api.RetrofitInstance
import ec.edu.epn.nanec.model.Evento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class EventosViewModel : ViewModel() {
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private lateinit var socket: Socket

    init {
        //Log.d("ViewModel", "ViewModel inicializado")
        cargarEventos()
        iniciarSocketIO()
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
    private fun iniciarSocketIO() {
        try {
            // Configuramos la conexión a Socket.IO (que se encuentra en nuestro backend)
            socket = IO.socket("http://172.31.40.52:8000")

            // Escucha el evento de conexión
            socket.on(Socket.EVENT_CONNECT) {
                Log.d("Socket.IO", "Conectado al servidor Socket.IO")
            }

            // Escucha eventos personalizados, en nuestro caso un nuevoEvento
            socket.on("nuevoEvento") { args ->
                if (args.isNotEmpty()) {
                    val eventoJson = args[0] as JSONObject
                    val evento = parsearEvento(eventoJson)
                    actualizarListaEventos(evento)
                }
            }

            // Escucha el evento de desconexión
            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("Socket.IO", "Desconectado del servidor Socket.IO")
            }

            // Conecta al servidor
            socket.connect()
        } catch (e: Exception) {
            Log.e("Socket.IO", "Error al iniciar Socket.IO: ${e.message}")
        }
    }
    // Se usa Gson para convertir el JSONObject en un objeto Evento
    private fun parsearEvento(eventoJson: JSONObject): Evento {

        val gson = com.google.gson.Gson()
        return gson.fromJson(eventoJson.toString(), Evento::class.java)
    }

    private fun actualizarListaEventos(evento: Evento) {
        viewModelScope.launch {
            _eventos.value = _eventos.value.toMutableList().apply { add(evento) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.close()
        }
    }
}