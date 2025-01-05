package ec.edu.epn.nanec.model

data class Evento(
    val _id: String,
    val nombre: String,
    val ubicacion: String,
    val descripcion: String,
    val tipo: String,
    val fecha: String,
    val imagenUrl: String
)