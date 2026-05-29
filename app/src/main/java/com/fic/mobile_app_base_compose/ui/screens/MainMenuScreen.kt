package com.fic.mobile_app_base_compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.navigation.Screen
import com.fic.mobile_app_base_compose.ui.theme.MaizeBrown
import com.fic.mobile_app_base_compose.ui.theme.MaizeGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController) {
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
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
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
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)))
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)))
        }
    }
}
