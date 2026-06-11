package com.fic.mobile_app_base_compose.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import com.fic.mobile_app_base_compose.data.model.MovimientoTipo
import com.fic.mobile_app_base_compose.data.model.ProductoMaiz
import com.fic.mobile_app_base_compose.data.model.UnidadMaiz
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeOrange
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController, viewModel: MaizViewModel) {
    val movimientos by viewModel.todosLosMovimientos.collectAsState()
    val productosBD by viewModel.todosLosProductos.collectAsState()
    val unidadesBD by viewModel.todasLasUnidades.collectAsState()

    val movimientosAgrupados = movimientos.groupBy { 
        it.fecha.substringAfter("/", "2025").let { res -> res.substringBefore(" ") + "/" + res } 
    }

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
                movimientosAgrupados.forEach { (mesAno, lista) ->
                    item {
                        Text(
                            text = if(mesAno.contains("/")) formatGroupHeader(mesAno) else stringResource(R.string.history_recent),
                            fontWeight = FontWeight.Bold,
                            color = MaizeGreen,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(lista) { movimiento ->
                        HistoryItem(movimiento, productosBD, unidadesBD)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    movimiento: MovimientoMaiz, 
    productosBD: List<ProductoMaiz>, 
    unidadesBD: List<UnidadMaiz>
) {
    val context = LocalContext.current
    val esEntrada = movimiento.tipo.equals(MovimientoTipo.ENTRADA, ignoreCase = true) || movimiento.tipo.equals("Entrada", ignoreCase = true)

    val productoTraducido = productosBD.find { it.clave == movimiento.producto }?.let { 
        stringResource(it.nombreRes) 
    } ?: movimiento.producto

    val unidadTraducida = unidadesBD.find { it.clave == movimiento.unidad }?.let { 
        stringResource(it.nombreRes) 
    } ?: movimiento.unidad

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = productoTraducido, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    
                    if (movimiento.latitud != null && movimiento.longitud != null) {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = stringResource(R.string.map_view_description),
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    val uri = "geo:${movimiento.latitud},${movimiento.longitud}?q=${movimiento.latitud},${movimiento.longitud}(${productoTraducido})"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                    context.startActivity(intent)
                                }
                        )
                    }
                }
                Text(text = movimiento.fecha, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                val cantidadTexto = "${if (esEntrada) "+" else "-"} ${movimiento.cantidad} $unidadTraducida"
                Text(
                    text = cantidadTexto,
                    fontWeight = FontWeight.Bold,
                    color = if (esEntrada) MaizeGreen else MaizeOrange
                )
            }
        }
    }
}

@Composable
fun formatGroupHeader(datePart: String): String {
    val parts = datePart.split("/")
    if (parts.size < 2) return datePart

    val month = parts[0]
    val year = parts[1]

    val monthName = when (month) {
        "01" -> stringResource(R.string.month_01)
        "02" -> stringResource(R.string.month_02)
        "03" -> stringResource(R.string.month_03)
        "04" -> stringResource(R.string.month_04)
        "05" -> stringResource(R.string.month_05)
        "06" -> stringResource(R.string.month_06)
        "07" -> stringResource(R.string.month_07)
        "08" -> stringResource(R.string.month_08)
        "09" -> stringResource(R.string.month_09)
        "10" -> stringResource(R.string.month_10)
        "11" -> stringResource(R.string.month_11)
        "12" -> stringResource(R.string.month_12)
        else -> month
    }

    return "$monthName $year"
}
