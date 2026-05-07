package com.rorycd.chatbot.ui.login

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.R
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.PasswordInputField
import com.rorycd.chatbot.ui.components.TextInputField
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Destination class for NavGraph route to [LoginScreen]
 */
object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_destination_title
}

/**
 * Screen where users can log in to their account
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRequireRegistration: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val failMsg = stringResource(R.string.login_failed)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Welcome message
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 64.dp)
            )
            Text(
                text = stringResource(R.string.lets_chat),
                style = MaterialTheme.typography.displayLarge
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
}
