package com.fic.mobile_app_base_compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeOrange
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController, viewModel: MaizViewModel) {
    // Observamos los movimientos reales de la base de datos
    val movimientos by viewModel.todosLosMovimientos.collectAsState()

    // Agrupamos por mes/año dinámicamente
    val movimientosAgrupados = movimientos.groupBy { it.fecha.substringAfter("/", "2025").let { it.substringBefore(" ") + "/" + it } }
    // Nota: La agrupación depende del formato de fecha que uses. Asumimos DD/MM/YYYY

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title), color = Color.White, fontWeight = FontWeight.Bold) },
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
        if (movimientos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.history_empty), color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(dimensionResource(R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                // Iteramos sobre los grupos (meses)
                movimientosAgrupados.forEach { (mesAno, lista) ->
                    item {
                        Text(
                            text = if(mesAno.contains("/")) formatGroupHeader(mesAno) else "Recientes",
                            fontWeight = FontWeight.Bold,
                            color = MaizeGreen,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(lista) { movimiento ->
                        HistoryItem(movimiento)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(movimiento: MovimientoMaiz) {
    val esEntrada = movimiento.tipo == "Entrada"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (esEntrada) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = if (esEntrada) MaizeGreen else MaizeOrange,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movimiento.producto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = movimiento.fecha, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (esEntrada) "+" else "-"} ${movimiento.cantidad} ${movimiento.unidad}",
                    fontWeight = FontWeight.Bold,
                    color = if (esEntrada) MaizeGreen else MaizeOrange
                )
            }
        }
    }
}

fun formatGroupHeader(datePart: String): String {
    // Intenta extraer el mes de DD/MM/YYYY
    return try {
        val parts = datePart.split("/")
        val month = parts[0]
        val year = parts[1]
        val monthName = when (month) {
            "01" -> "Enero"
            "02" -> "Febrero"
            "03" -> "Marzo"
            "04" -> "Abril"
            "05" -> "Mayo"
            "06" -> "Junio"
            "07" -> "Julio"
            "08" -> "Agosto"
            "09" -> "Septiembre"
            "10" -> "Octubre"
            "11" -> "Noviembre"
            "12" -> "Diciembre"
            else -> "Mes $month"
        }
        "$monthName $year"
    } catch (e: Exception) {
        datePart
    }
}
