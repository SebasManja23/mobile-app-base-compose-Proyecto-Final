package com.fic.mobile_app_base_compose.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradasScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val context = LocalContext.current
    var productoSeleccionado by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadSeleccionada by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    val productos = listOf("Maíz", "Maíz quebrado", "Mochote", "Tamo")
    val unidades = listOf("kg", "Toneladas", "Sacos")

    // Lógica para el Calendario
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Validación: ¿Está todo lleno?
    val esFormularioValido = productoSeleccionado.isNotEmpty() &&
            cantidad.isNotEmpty() &&
            unidadSeleccionada.isNotEmpty() &&
            fecha.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_entradas), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaizeGreen)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_screen))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_fields))
        ) {
            CornFormField(
                label = stringResource(R.string.form_type_label),
                value = productoSeleccionado,
                placeholder = stringResource(R.string.form_type_hint),
                icon = Icons.Default.Agriculture,
                isDropdown = true,
                options = productos,
                onOptionSelected = { productoSeleccionado = it }
            )

            CornFormField(
                label = stringResource(R.string.form_qty_label),
                value = cantidad,
                placeholder = stringResource(R.string.form_qty_hint),
                icon = Icons.Default.Scale,
                keyboardType = KeyboardType.Number,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) cantidad = input
                }
            )

            CornFormField(
                label = stringResource(R.string.form_unit_label),
                value = unidadSeleccionada,
                placeholder = stringResource(R.string.form_unit_hint),
                icon = Icons.Default.Scale,
                isDropdown = true,
                options = unidades,
                onOptionSelected = { unidadSeleccionada = it }
            )

            // Campo de Fecha
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.form_date_label), fontWeight = FontWeight.Bold, color = MaizeGreen, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    enabled = false,
                    placeholder = { Text(stringResource(R.string.form_date_hint), color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFDAA520)) },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledTextColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        disabledIndicatorColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_large)))

            Button(
                onClick = {
                    if (esFormularioValido) {
                        viewModel.guardarMovimiento(
                            producto = productoSeleccionado,
                            tipo = "Entrada",
                            cantidad = cantidad,
                            unidad = unidadSeleccionada,
                            fecha = fecha
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.btn_height)),
                enabled = esFormularioValido,
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaizeGreen,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null)
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    Text(stringResource(R.string.btn_reg_entrada), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CornFormField(
    label: String,
    value: String,
    placeholder: String,
    icon: ImageVector,
    isDropdown: Boolean = false,
    options: List<String> = emptyList(),
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {},
    onOptionSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, color = MaizeGreen, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

        Box {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder, color = Color.Gray) },
                leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFFDAA520)) },
                trailingIcon = {
                    if (isDropdown) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                },
                readOnly = isDropdown,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = MaizeGreen
                )
            )

            if (isDropdown) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
