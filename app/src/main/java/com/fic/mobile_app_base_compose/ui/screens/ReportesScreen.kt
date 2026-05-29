package com.fic.mobile_app_base_compose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(navController: NavHostController, viewModel: MaizViewModel) {
    // 1. Obtenemos los movimientos reales de la base de datos
    val movimientos by viewModel.todosLosMovimientos.collectAsState()

    // 2. Preparamos los datos para "Movimientos del día" (tomamos los últimos 10)
    val ultimosMovimientos = movimientos.take(10).map { mov ->
        listOf(mov.producto, mov.tipo, mov.cantidad, mov.unidad, mov.fecha)
    }

    // 3. Calculamos el "Inventario Total" dinámicamente por producto con conversión de unidades
    val productosNombres = listOf("Maíz", "Maíz quebrado", "Mochote", "Tamo")

    val inventarioCalculado = productosNombres.map { nombreProd ->
        // Sumamos todo convirtiendo a KG primero para no perder ni un gramo de precisión
        val totalKg = movimientos.filter { it.producto == nombreProd }.sumOf { mov ->
            val cant = mov.cantidad.toDoubleOrNull() ?: 0.0
            val factor = when (mov.unidad) {
                "Toneladas" -> 1000.0
                "Sacos" -> 50.0
                else -> 1.0 // kg
            }
            val enKg = cant * factor
            if (mov.tipo == "Entrada") enKg else -enKg
        }

        // Formateo de alta precisión:
        // Si el valor es mayor a 1 Tonelada, mostramos 3 decimales (el 3er decimal es el kilo exacto)
        if (totalKg >= 1000 || totalKg <= -1000) {
            listOf(nombreProd, String.format(Locale.getDefault(), "%.3f", totalKg / 1000.0), "Ton")
        } else {
            // Para kilos usamos 2 decimales por si hay fracciones
            listOf(nombreProd, String.format(Locale.getDefault(), "%.2f", totalKg), "kg")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.report_title), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filtrar */ }) {
                        Icon(Icons.Default.FilterAlt, contentDescription = stringResource(R.string.filter_description), tint = Color.White)
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
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.margin_large))
        ) {
            // Sección Movimientos Reales
            ReportSection(
                title = stringResource(R.string.report_daily_movements),
                icon = Icons.Default.CalendarMonth,
                headers = listOf(
                    stringResource(R.string.report_header_product),
                    stringResource(R.string.report_header_type),
                    stringResource(R.string.report_header_qty),
                    stringResource(R.string.report_header_unit),
                    stringResource(R.string.report_header_date)
                ),
                data = ultimosMovimientos
            )

            // Sección Inventario Calculado Inteligente
            ReportSection(
                title = stringResource(R.string.report_total_inventory),
                icon = Icons.AutoMirrored.Filled.Assignment,
                headers = listOf(
                    stringResource(R.string.report_header_product),
                    stringResource(R.string.report_header_qty_total),
                    stringResource(R.string.report_header_unit)
                ),
                data = inventarioCalculado
            )
        }
    }
}

@Composable
fun ReportSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    headers: List<String>,
    data: List<List<String>>
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFFDAA520), modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)))
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
            Text(text = title, fontWeight = FontWeight.Bold, color = MaizeGreen, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_medium)))

        Card(
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header de la tabla
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaizeGreen)
                        .padding(dimensionResource(R.dimen.spacing_small))
                ) {
                    headers.forEach { header ->
                        Text(
                            text = header,
                            modifier = Modifier.weight(1f),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Filas de datos
                if (data.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_records),
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                } else {
                    data.forEachIndexed { index, row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (index % 2 == 0) Color.White else Color(0xFFF9F9F9))
                                .padding(dimensionResource(R.dimen.spacing_small))
                        ) {
                            row.forEach { cell ->
                                Text(
                                    text = cell,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 11.sp,
                                    color = if (cell == "Entrada") MaizeGreen else if (cell == "Salida") Color(0xFFF57C00) else Color.DarkGray,
                                    fontWeight = if (cell == "Entrada" || cell == "Salida") FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                        if (index < data.size - 1) {
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}
