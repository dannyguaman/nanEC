package ec.edu.epn.nanec.uin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit,
                onGoogleLogin: () -> Unit,
                errorMessage: String?
                ) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column {
        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") }
        )
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLoginSuccess(correo, contrasena) }) {
            Text("Iniciar sesión con correo")
        }
        Spacer(modifier = Modifier.height(8 .dp))
        Button(onClick = onGoogleLogin) {
            Text("Iniciar sesión con Google")
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
