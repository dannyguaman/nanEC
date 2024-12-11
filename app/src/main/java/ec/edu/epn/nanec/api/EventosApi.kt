package ec.edu.epn.nanec.api
import ec.edu.epn.nanec.model.Evento
import ec.edu.epn.nanec.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventosApi {
    @GET("/")
    suspend fun obtenerEventos(): List<Evento>
    @POST("/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario )
}
