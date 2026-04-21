package com.rorycd.learningassistant.ui

import android.app.Application
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.ui.components.PasswordInputField
import com.rorycd.learningassistant.ui.components.TextInputField
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * Represents the current state of the [LoginScreen] UI
 */
@Parcelize
data class WelcomeUiState(
    val username: String,
    val password: String
) : Parcelable

@Composable
fun LoginScreen(
    repo: UserRepository,
    onLoginSuccess: () -> Unit,
    onRequireRegistration: () -> Unit
) {
    var uiState by rememberSaveable { mutableStateOf(WelcomeUiState("", "")) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    ) {
        // Welcome message
        Text(
            text = stringResource(R.string.welcome_message)
        )
        Text(
            text = stringResource(R.string.student)
        )
        Text(
            text = stringResource(R.string.learning_message)
        )

        // Login fields
        TextInputField(
            value = uiState.username,
            onValueChange = {
                uiState = uiState.copy(username = it)
            },
            label = stringResource(R.string.username_label)
        )
        PasswordInputField(
            value = uiState.password,
            onValueChange = { uiState = uiState.copy(password = it) },
            label = stringResource(R.string.password_label)
        )
        Button(
            onClick = {
                // Try to log in
                scope.launch {
                    val success = repo.login(uiState.username, uiState.password)

                    if (success) onLoginSuccess()
                    else Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(stringResource(R.string.login_button))
        }

        // Need an account
        TextButton(
            onClick = onRequireRegistration
        ) {
            Text(stringResource(R.string.need_account_message))
        }
    }
}
