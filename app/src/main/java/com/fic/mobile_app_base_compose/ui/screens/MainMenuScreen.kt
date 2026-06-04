package com.fic.mobile_app_base_compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.navigation.Screen
import com.fic.mobile_app_base_compose.ui.theme.MaizeBrown
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeOrange
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController, viewModel: MaizViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_title), color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaizeGreen)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            CornPriceCard(viewModel)

            MenuButton(
                title = stringResource(R.string.menu_entradas),
                subtitle = stringResource(R.string.menu_entradas_sub),
                icon = Icons.Default.VerticalAlignBottom,
                color = MaizeGreen,
                onClick = { navController.navigate(Screen.Entradas.route) }
            )
            MenuButton(
                title = stringResource(R.string.menu_salidas),
                subtitle = stringResource(R.string.menu_salidas_sub),
                icon = Icons.Default.VerticalAlignTop,
                color = MaizeOrange,
                onClick = { navController.navigate(Screen.Salidas.route) }
            )
            MenuButton(
                title = stringResource(R.string.menu_reportes),
                subtitle = stringResource(R.string.menu_reportes_sub),
                icon = Icons.Default.BarChart,
                color = MaizeBrown,
                onClick = { navController.navigate(Screen.Reportes.route) }
            )
            MenuButton(
                title = stringResource(R.string.menu_historial),
                subtitle = stringResource(R.string.menu_historial_sub),
                icon = Icons.Default.History,
                color = Color(0xFF455A64),
                onClick = { navController.navigate(Screen.History.route) }
            )
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Composable
fun CornPriceCard(viewModel: MaizViewModel) {
    val cornPrice by viewModel.cornPrice.collectAsState()
    val isLoading by viewModel.isLoadingPrice.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Default.TrendingUp, 
                    contentDescription = null, 
                    tint = Color(0xFFDAA520),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = stringResource(R.string.price_card_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    if (cornPrice != null) {
                        val displayProductName = when {
                            cornPrice!!.name.contains("Maíz", true) || cornPrice!!.name.contains("Corn", true) -> 
                                stringResource(R.string.val_maiz)
                            else -> cornPrice!!.name
                        }
                        Text(
                            text = displayProductName, 
                            fontSize = 10.sp, 
                            color = Color.Gray,
                            maxLines = 1
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            } else if (cornPrice != null) {
                Column(horizontalAlignment = Alignment.End) {
                    val priceValue = cornPrice!!.data.firstOrNull()?.value ?: "N/A"
                    Text(
                        text = "$priceValue USD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaizeGreen
                    )
                    val dateLabel = cornPrice!!.data.firstOrNull()?.date ?: ""
                    Text(
                        text = stringResource(R.string.price_last_update, dateLabel),
                        fontSize = 9.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.menu_card_height)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large))
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp, lineHeight = 16.sp)
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos, 
                contentDescription = null, 
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
            )
        }
    }
}
