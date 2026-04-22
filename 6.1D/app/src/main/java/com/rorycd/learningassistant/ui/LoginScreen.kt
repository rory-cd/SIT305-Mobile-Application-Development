package com.rorycd.learningassistant.ui

import android.graphics.fonts.FontStyle
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.ui.components.PasswordInputField
import com.rorycd.learningassistant.ui.components.TextInputField
import com.rorycd.learningassistant.ui.theme.LearningAssistantTheme
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
            value = uiState.username,
            onValueChange = {
                uiState = uiState.copy(username = it)
            },
            label = stringResource(R.string.username_label),
            modifier = Modifier.padding(top = 32.dp)
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
                    val success = repo.login(uiState.username.trim(), uiState.password)

                    if (success) onLoginSuccess()
                    else {
                        Toast.makeText(context, failMsg, Toast.LENGTH_SHORT).show()
                    }
                }
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

@Preview
@Composable
fun LoginScreenPreview() {
    LearningAssistantTheme {
        LoginScreen(UserRepository(LocalContext.current), {}, {})
    }
}
