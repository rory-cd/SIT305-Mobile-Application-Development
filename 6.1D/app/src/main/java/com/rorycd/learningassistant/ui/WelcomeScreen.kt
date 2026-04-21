package com.rorycd.learningassistant.ui

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.ui.components.PasswordInputField
import com.rorycd.learningassistant.ui.components.TextInputField
import kotlinx.parcelize.Parcelize

/**
 * Represents the current state of the [WelcomeScreen] UI
 */
@Parcelize
data class WelcomeUiState(
    val username: String,
    val password: String
) : Parcelable

@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    var uiState by rememberSaveable { mutableStateOf(WelcomeUiState("", "")) }

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
            onValueChange = { uiState = uiState.copy(username = it) },
            label = stringResource(R.string.username_label)
        )
        PasswordInputField(
            value = uiState.password,
            onValueChange = { uiState = uiState.copy(password = it) },
            label = stringResource(R.string.password_label)
        )
        Button(
            onClick = onLogin
        ) {
            Text(stringResource(R.string.login_button))
        }

        // Need an account
        TextButton(
            onClick = onRegister
        ) {
            Text(stringResource(R.string.need_account_message))
        }
    }
}
