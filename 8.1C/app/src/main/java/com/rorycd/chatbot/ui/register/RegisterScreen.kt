package com.rorycd.chatbot.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.chatbot.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.PasswordInputField
import com.rorycd.chatbot.ui.components.TextInputField

/**
 * Destination class for NavGraph route to [RegisterScreen]
 */
object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.register_destination_title
}

/**
 * Screen to allow users to create an account
 */
@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val failMsg = stringResource(R.string.username_taken)
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Message
            Text(
                text = stringResource(R.string.register_message),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Form fields
            TextInputField(
                value = state.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = stringResource(R.string.username_label),
                isError = state.usernameError != null,
                errorMessage = state.usernameError?.let { stringResource(it) } ?: "",
                modifier = Modifier
                    .padding(top = 24.dp)
            )
            PasswordInputField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = stringResource(R.string.password_label),
                isError = state.passwordError != null,
                errorMessage = state.passwordError?.let { stringResource(it) } ?: ""
            )
            PasswordInputField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = stringResource(R.string.confirm_password_label),
                isError = state.confirmPasswordError != null,
                errorMessage = state.confirmPasswordError?.let { stringResource(it) } ?: ""
            )
            Button(
                onClick = {
                    viewModel.register(
                        onSuccess = { onRegistrationSuccess() },
                        onFailure = { Toast.makeText(context, failMsg, Toast.LENGTH_SHORT).show() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    stringResource(R.string.register_button),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
