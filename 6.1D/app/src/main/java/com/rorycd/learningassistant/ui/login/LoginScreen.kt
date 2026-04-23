package com.rorycd.learningassistant.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.PasswordInputField
import com.rorycd.learningassistant.ui.components.TextInputField

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_destination_title
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRequireRegistration: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val failMsg = stringResource(R.string.login_failed)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Welcome message
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 64.dp)
        )
        Text(
            text = stringResource(R.string.student),
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = stringResource(R.string.learning_message),
            style = MaterialTheme.typography.displaySmall
        )

        // Login fields
        TextInputField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            label = stringResource(R.string.username_label),
            modifier = Modifier.padding(top = 32.dp)
        )
        PasswordInputField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = stringResource(R.string.password_label)
        )
        Button(
            enabled = state.isValid,
            onClick = {
                viewModel.login(
                    onSuccess = onLoginSuccess,
                    onFailure = { Toast.makeText(context, failMsg, Toast.LENGTH_SHORT).show() }
                );
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.login_button),
                fontWeight = FontWeight.Bold
            )
        }

        // Need an account
        TextButton(
            onClick = onRequireRegistration,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.need_account_message))
        }
    }
}
