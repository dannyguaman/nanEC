package ec.edu.epn.nanec.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.edu.epn.nanec.uin.DetalleEventoScreen
import ec.edu.epn.nanec.uin.ListaEventosScreen
import ec.edu.epn.nanec.viewmodel.EventosViewModel
import ec.edu.epn.nanec.uin.SeleccionTipoSuscripcionScreen
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel,
    eventosViewModel: EventosViewModel
) {
    NavHost(navController = navController, startDestination = "lista_eventos") {
        composable("lista_eventos") {
            ListaEventosScreen(eventosViewModel, navController)
        }
        composable("seleccion_suscripcion") {
            SeleccionTipoSuscripcionScreen(usuarioViewModel) {
                navController.navigate("lista_eventos")
            }
        }
        composable("detalle_evento/{eventoId}") { backStackEntry ->
            val eventoId = backStackEntry.arguments?.getString("eventoId")
            DetalleEventoScreen(eventoId, eventosViewModel)
        }
    }
}
