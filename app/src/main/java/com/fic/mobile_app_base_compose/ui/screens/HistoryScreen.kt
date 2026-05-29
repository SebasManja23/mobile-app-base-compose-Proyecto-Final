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
    val movimientos by viewModel.todosLosMovimientos.collectAsState()

    // Agrupación robusta para evitar crashes
    val movimientosAgrupados = movimientos.groupBy { mov ->
        if (mov.fecha.contains("/")) {
            val parts = mov.fecha.split("/")
            if (parts.size >= 3) "${parts[1]}/${parts[2]}" else "Recientes"
        } else "Recientes"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
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
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movimientosAgrupados.forEach { (mesAno, lista) ->
                    item {
                        Text(text = mesAno, fontWeight = FontWeight.Bold, color = MaizeGreen, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(lista) { mov ->
                        HistoryItem(mov)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (esEntrada) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = if (esEntrada) MaizeGreen else MaizeOrange
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movimiento.producto, fontWeight = FontWeight.Bold)
                Text(text = movimiento.fecha, fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                text = "${if (esEntrada) "+" else "-"} ${movimiento.cantidad} ${movimiento.unidad}",
                fontWeight = FontWeight.Bold,
                color = if (esEntrada) MaizeGreen else MaizeOrange
            )
        }
    }
}