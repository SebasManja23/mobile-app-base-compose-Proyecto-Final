package com.fic.mobile_app_base_compose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.navigation.Screen
import com.fic.mobile_app_base_compose.ui.theme.ButtonGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeYellow
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val esValido = usuario.isNotEmpty() && contrasena.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_screen)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Surface(
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size)),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaizeYellow
        ) { }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_top_logo)))

        Text(
            text = stringResource(R.string.login_welcome),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_top_fields)))

        // Campo Usuario
        Text(
            text = stringResource(R.string.login_user_label),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = { Text(stringResource(R.string.login_user_hint)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5)
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_fields)))

        // Campo Contraseña
        Text(
            text = stringResource(R.string.login_pass_label),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text(stringResource(R.string.login_pass_hint)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón Iniciar Sesión con Validación Real
        Button(
            onClick = {
                if (esValido) {
                    scope.launch {
                        val user = viewModel.login(usuario, contrasena)
                        if (user != null) {
                            navController.navigate(Screen.MainMenu.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = esValido,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.btn_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGreen,
                disabledContainerColor = Color.LightGray
            )
        ) {
            Text(text = stringResource(R.string.login_btn), fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text(text = stringResource(R.string.login_no_account), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
    }
}
