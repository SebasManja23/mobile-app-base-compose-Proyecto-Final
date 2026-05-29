package com.fic.mobile_app_base_compose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.fic.mobile_app_base_compose.data.model.Usuario
import com.fic.mobile_app_base_compose.ui.theme.ButtonGreen
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val esValido = nombre.isNotEmpty() && correo.isNotEmpty() && usuario.isNotEmpty() && contrasena.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reg_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_screen))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.reg_subtitle),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.margin_top_logo))
            )

            // Campo Nombre
            CustomTextField(
                label = stringResource(R.string.reg_name_label),
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = stringResource(R.string.reg_name_hint)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_fields)))

            // Campo Correo
            CustomTextField(
                label = stringResource(R.string.reg_email_label),
                value = correo,
                onValueChange = { correo = it },
                placeholder = stringResource(R.string.reg_email_hint)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_fields)))

            // Campo Usuario
            CustomTextField(
                label = stringResource(R.string.reg_user_label),
                value = usuario,
                onValueChange = { usuario = it },
                placeholder = stringResource(R.string.reg_user_hint)
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
                placeholder = { Text(stringResource(R.string.reg_pass_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_top_fields)))

            // Botón Registrarse
            Button(
                onClick = {
                    if (esValido) {
                        scope.launch {
                            val exito = viewModel.registrarUsuario(
                                Usuario(
                                    nombreUsuario = usuario,
                                    nombreCompleto = nombre,
                                    correo = correo,
                                    contrasena = contrasena
                                )
                            )
                            if (exito) {
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // Regresa al Login
                            } else {
                                Toast.makeText(context, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.btn_height)),
                enabled = esValido,
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(
                    text = stringResource(R.string.reg_btn),
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(text = label, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5)
            )
        )
    }
}
