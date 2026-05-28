package com.fic.mobile_app_base_compose.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.ui.navigation.Screen
import com.fic.mobile_app_base_compose.ui.theme.ButtonGreen
import com.fic.mobile_app_base_compose.ui.theme.MaizeYellow
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel
@Composable
fun LoginScreen(navController: NavHostController,viewModel: MaizViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_screen)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size)),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaizeYellow
        ) { }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        Text(text = stringResource(R.string.login_welcome), fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_fields)))

        OutlinedTextField(
            value = "", onValueChange = {},
            label = { Text(stringResource(R.string.login_user_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        Button(
            onClick = { /* Navegación temporal */ },
            modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.btn_height)),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
        ) {
            Text(text = stringResource(R.string.login_btn))
        }

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text(text = stringResource(R.string.login_no_account))
        }
    }
}
