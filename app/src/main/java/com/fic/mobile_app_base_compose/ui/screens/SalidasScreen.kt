package com.fic.mobile_app_base_compose.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.data.model.MovimientoTipo
import com.fic.mobile_app_base_compose.ui.theme.MaizeOrange
import com.fic.mobile_app_base_compose.util.SelectionOption
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalidasScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val context = LocalContext.current
    val movimientos by viewModel.todosLosMovimientos.collectAsState()

    var productoKey by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var unidadKey by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    val productos = remember {
        listOf(
            SelectionOption(R.string.val_maiz, "Maíz"),
            SelectionOption(R.string.val_maiz_quebrado, "Maíz quebrado"),
            SelectionOption(R.string.val_mochote, "Mochote"),
            SelectionOption(R.string.val_tamo, "Tamo")
        )
    }
    
    val unidades = remember {
        listOf(
            SelectionOption(R.string.unit_kg, "kg"),
            SelectionOption(R.string.unit_sacks, "Sacos"),
            SelectionOption(R.string.unit_tons, "Toneladas")
        )
    }

    val productoDisplay = productos.find { it.key == productoKey }?.let { stringResource(it.labelRes) } ?: ""
    val unidadDisplay = unidades.find { it.key == unidadKey }?.let { stringResource(it.labelRes) } ?: ""

    val stockDisponibleKg = remember(productoKey, movimientos) {
        if (productoKey.isEmpty()) 0.0
        else {
            movimientos.filter { it.producto == productoKey }.sumOf { mov ->
                val cant = mov.cantidad.toDoubleOrNull() ?: 0.0
                val factor = when (mov.unidad) {
                    "Toneladas" -> 1000.0
                    "Sacos" -> 50.0
                    else -> 1.0
                }
                // COMPARACIÓN COMPATIBLE: Acepta "Entrada" y "ENTRADA"
                val esEntrada = mov.tipo.equals(MovimientoTipo.ENTRADA, ignoreCase = true) || mov.tipo.equals("Entrada", ignoreCase = true)
                if (esEntrada) cant * factor else -(cant * factor)
            }
        }
    }

    val cantidadIngresadaKg = remember(cantidad, unidadKey) {
        val cant = cantidad.toDoubleOrNull() ?: 0.0
        val factor = when (unidadKey) {
            "Toneladas" -> 1000.0
            "Sacos" -> 50.0
            else -> 1.0
        }
        cant * factor
    }

    val stockInsuficiente = productoKey.isNotEmpty() && cantidad.isNotEmpty() && cantidadIngresadaKg > stockDisponibleKg

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

    val esFormularioValido = productoKey.isNotEmpty() && cantidad.isNotEmpty() && unidadKey.isNotEmpty() && fecha.isNotEmpty()
    val sePuedeGuardar = esFormularioValido && !stockInsuficiente && stockDisponibleKg > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_salidas), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaizeOrange)
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
            if (productoKey.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if(stockDisponibleKg > 0) Color(0xFFFFF3E0) else Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = if(stockDisponibleKg > 0) MaizeOrange else Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if(stockDisponibleKg > 0) "Stock disponible: $stockDisponibleKg kg" else "Sin existencias en inventario",
                            fontWeight = FontWeight.Bold,
                            color = if(stockDisponibleKg > 0) Color.DarkGray else Color.Red
                        )
                    }
                }
            }

            CornFormField(
                label = stringResource(R.string.form_type_label),
                value = productoDisplay,
                placeholder = stringResource(R.string.form_type_hint),
                icon = Icons.Default.Agriculture,
                isDropdown = true,
                options = productos.map { context.getString(it.labelRes) },
                onOptionSelected = { selectedLabel ->
                    productoKey = productos.find { context.getString(it.labelRes) == selectedLabel }?.key ?: ""
                }
            )

            Column {
                CornFormField(
                    label = stringResource(R.string.form_qty_label),
                    value = cantidad,
                    placeholder = stringResource(R.string.form_qty_hint),
                    icon = Icons.Default.Scale,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { input -> if (input.all { it.isDigit() || it == '.' }) cantidad = input }
                )
                if (stockInsuficiente) {
                    Text(
                        text = "Cantidad excede el stock (Máx: $stockDisponibleKg kg)",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            CornFormField(
                label = stringResource(R.string.form_unit_label),
                value = unidadDisplay,
                placeholder = stringResource(R.string.form_unit_hint),
                icon = Icons.Default.Scale,
                isDropdown = true,
                options = unidades.map { context.getString(it.labelRes) },
                onOptionSelected = { selectedLabel ->
                    unidadKey = unidades.find { context.getString(it.labelRes) == selectedLabel }?.key ?: ""
                }
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.form_date_label), fontWeight = FontWeight.Bold, color = MaizeOrange, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                    enabled = false,
                    placeholder = { Text(stringResource(R.string.form_date_hint), color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFDAA520)) },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledTextColor = Color.Black,
                        disabledIndicatorColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_large)))

            Button(
                onClick = {
                    if (sePuedeGuardar) {
                        viewModel.guardarMovimiento(productoKey, MovimientoTipo.SALIDA, cantidad, unidadKey, fecha)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.btn_height)),
                enabled = sePuedeGuardar,
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaizeOrange,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    Text(stringResource(R.string.btn_reg_salida), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
