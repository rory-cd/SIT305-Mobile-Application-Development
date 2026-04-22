package com.rorycd.learningassistant.ui

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.ui.components.PasswordInputField
import com.rorycd.learningassistant.ui.components.TextInputField
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import com.rorycd.learningassistant.ui.theme.LearningAssistantTheme
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * Represents the current state of the [RegisterScreen] UI
 */
@Parcelize
data class RegisterUiState(
    val username: String = "",
    val usernameError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: Int? = null,
    val imgUri: String = ""
) : Parcelable

@Composable
fun RegisterScreen(
    repo: UserRepository,
    onRegistrationSuccess: () -> Unit
) {
    var uiState by rememberSaveable { mutableStateOf(RegisterUiState()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Form validation
    suspend fun formIsValid(): Boolean {
        var result = true

        // Check if username already exists, or is empty
        if (repo.userExists(uiState.username)) {
            uiState = uiState.copy(usernameError = R.string.username_taken)
            result = false
        } else if (uiState.username.isEmpty()) {
            uiState = uiState.copy(usernameError = R.string.username_empty)
            result = false
        }
        // Check if email is empty or missing "@"
        if (uiState.email.isEmpty()) {
            uiState = uiState.copy(emailError = R.string.email_empty)
            result = false
        } else if (!uiState.email.contains("@")) {
            uiState = uiState.copy(emailError = R.string.email_contains)
            result = false
        }
        // Check password is long enough and matches
        if (uiState.password.length < 6) {
            uiState = uiState.copy(passwordError = R.string.password_length)
            result = false
        }
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(confirmPasswordError = R.string.password_match)
            result = false
        }
        return result
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Message
        Text(
            text = stringResource(R.string.register_message),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 32.dp)
        )

        // Image
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            val imageSelectLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    uiState = uiState.copy(imgUri = uri.toString())
                }
            }
            AsyncImage(
                model = uiState.imgUri,
                contentDescription = stringResource(id = R.string.user_image_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
            IconButton(
                onClick = { imageSelectLauncher.launch(
                    input = PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = stringResource(R.string.checkmark_desc),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Form fields
        TextInputField(
            value = uiState.username,
            onValueChange = { uiState = uiState.copy(username = it, usernameError = null) },
            label = stringResource(R.string.username_label),
            isError = uiState.usernameError != null,
            errorMessage = uiState.usernameError?.let { stringResource(it) } ?: "",
            modifier = Modifier
                .padding(top = 24.dp)
        )
        TextInputField(
            value = uiState.email,
            onValueChange = { uiState = uiState.copy(email = it, emailError = null) },
            label = stringResource(R.string.email_label),
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError?.let { stringResource(it) } ?: ""
        )
        PasswordInputField(
            value = uiState.password,
            onValueChange = { uiState = uiState.copy(password = it, passwordError = null) },
            label = stringResource(R.string.password_label),
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError?.let { stringResource(it) } ?: ""
        )
        PasswordInputField(
            value = uiState.confirmPassword,
            onValueChange = { uiState = uiState.copy(confirmPassword = it, confirmPasswordError = null) },
            label = stringResource(R.string.confirm_password_label),
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError?.let { stringResource(it) } ?: ""
        )
        Button(
            onClick = {
                scope.launch {
                    if (formIsValid()) {
                        val success = repo.register(
                            uiState.username.trim(),
                            uiState.password,
                            uiState.email.trim(),
                            uiState.imgUri
                        )
                        if (success) onRegistrationSuccess()
                    }
                }
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
