package ec.edu.epn.nanec.api
import ec.edu.epn.nanec.model.Evento
import retrofit2.http.GET

interface EventosApi {
    @GET("/")
    suspend fun obtenerEventos(): List<Evento>
}
