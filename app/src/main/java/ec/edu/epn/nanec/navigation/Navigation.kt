package ec.edu.epn.nanec.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.edu.epn.nanec.MainActivity
import ec.edu.epn.nanec.uin.DetalleEventoScreen
import ec.edu.epn.nanec.uin.ListaEventosScreen
import ec.edu.epn.nanec.uin.LoginScreen
import ec.edu.epn.nanec.uin.MapaEventosScreen
import ec.edu.epn.nanec.viewmodel.EventosViewModel
import ec.edu.epn.nanec.uin.SeleccionTipoSuscripcionScreen
import ec.edu.epn.nanec.viewmodel.AuthViewModel
import ec.edu.epn.nanec.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel,
    eventosViewModel: EventosViewModel,
    authViewModel: AuthViewModel
) {
    val usuarioAutenticado by authViewModel.usuarioAutenticado.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    //a esto se lo conoce como navegación condicional (observen que depende del valor del estado)
    LaunchedEffect(usuarioAutenticado) {
        if(usuarioAutenticado){
            navController.navigate("lista_eventos"){
                popUpTo("login") { inclusive = true }  //se elimina la pantalla login del stack
            }
        }else{
            navController.navigate("login") {
                popUpTo("lista_eventos") { inclusive = true } //se elimina la pantala lista_eventos del stack
            }
        }
    }
    NavHost(navController = navController, startDestination = if (usuarioAutenticado) "lista_eventos" else "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { correo, contrasena ->
                    authViewModel.iniciarSesion(correo, contrasena)},
                onGoogleLogin = {(navController.context as? MainActivity)?.iniciarGoogleOneTap()},
                errorMessage = errorMessage
            )
        }

        composable("lista_eventos") {
            ListaEventosScreen(eventosViewModel, navController, authViewModel)
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
        composable("mapa_eventos"){
            MapaEventosScreen(eventosViewModel, usuarioViewModel)
        }
    }
}
