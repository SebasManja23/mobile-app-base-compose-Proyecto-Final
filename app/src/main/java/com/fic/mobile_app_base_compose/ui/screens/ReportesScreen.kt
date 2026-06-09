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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.data.model.MovimientoTipo
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val context = LocalContext.current
    val movimientos by viewModel.todosLosMovimientos.collectAsState()
    val productosBD by viewModel.todosLosProductos.collectAsState()

    val txtEntrada = stringResource(R.string.menu_entradas)
    val txtSalida = stringResource(R.string.menu_salidas)

    val ultimosMovimientos = movimientos.take(10).map { mov ->
        val esEntrada = mov.tipo.equals(MovimientoTipo.ENTRADA, ignoreCase = true) || mov.tipo.equals("Entrada", ignoreCase = true)
        
        // Buscamos el nombre traducido del producto basado en la BD
        val prodTraducido = productosBD.find { it.clave == mov.producto }?.let { 
            context.getString(it.nombreRes) 
        } ?: mov.producto

        listOf(
            prodTraducido,
            if (esEntrada) txtEntrada else txtSalida,
            mov.cantidad,
            mov.unidad,
            mov.fecha
        )
    }

    val inventarioCalculado = productosBD.map { producto ->
        val totalKg = movimientos.filter { it.producto == producto.clave }.sumOf { mov ->
            val cant = mov.cantidad.toDoubleOrNull() ?: 0.0
            val factor = when (mov.unidad) {
                "Toneladas" -> 1000.0
                "Sacos" -> 50.0
                else -> 1.0
            }
            val esEntrada = mov.tipo.equals(MovimientoTipo.ENTRADA, ignoreCase = true) || mov.tipo.equals("Entrada", ignoreCase = true)
            if (esEntrada) cant * factor else -(cant * factor)
        }

        val prodTraducido = context.getString(producto.nombreRes)
        if (totalKg >= 1000 || totalKg <= -1000) {
            listOf(prodTraducido, String.format(Locale.getDefault(), "%.3f", totalKg / 1000.0), stringResource(R.string.unit_ton))
        } else {
            listOf(prodTraducido, String.format(Locale.getDefault(), "%.2f", totalKg), stringResource(R.string.unit_kg))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.report_title), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FilterAlt, contentDescription = stringResource(R.string.filter_description), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaizeGreen)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(dimensionResource(R.dimen.padding_medium)).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.margin_large))
        ) {
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
fun ReportSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, headers: List<String>, data: List<List<String>>) {
    val txtEntrada = stringResource(R.string.menu_entradas)
    val txtSalida = stringResource(R.string.menu_salidas)

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFFDAA520), modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)))
            Spacer(Modifier.width(dimensionResource(R.dimen.spacing_small)))
            Text(title, fontWeight = FontWeight.Bold, color = MaizeGreen, fontSize = 16.sp)
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.margin_medium)))
        Card(
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
        ) {
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().background(MaizeGreen).padding(dimensionResource(R.dimen.spacing_small))) {
                    headers.forEach { Text(it, Modifier.weight(1f), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                }
                if (data.isEmpty()) {
                    Text(stringResource(R.string.no_records), Modifier.padding(16.dp).align(Alignment.CenterHorizontally), color = Color.Gray, fontSize = 12.sp)
                } else {
                    data.forEachIndexed { index, row ->
                        Row(Modifier.fillMaxWidth().background(if (index % 2 == 0) Color.White else Color(0xFFF9F9F9)).padding(dimensionResource(R.dimen.spacing_small))) {
                            row.forEach { cell ->
                                Text(
                                    cell, Modifier.weight(1f), fontSize = 11.sp,
                                    color = when (cell) {
                                        txtEntrada -> MaizeGreen
                                        txtSalida -> Color(0xFFF57C00)
                                        else -> Color.DarkGray
                                    },
                                    fontWeight = if (cell == txtEntrada || cell == txtSalida) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
