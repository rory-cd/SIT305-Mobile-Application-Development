package com.rorycd.learningassistant.ui

import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * Represents the current state of the [RegisterScreen] UI
 */
@Parcelize
data class RegisterUiState(
    val username: String = "",
    val usernameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val imgUri: String = ""
) : Parcelable

@Composable
fun RegisterScreen(
    repo: UserRepository,
    onRegistrationSuccess: () -> Unit
) {
    var uiState by rememberSaveable { mutableStateOf(RegisterUiState()) }
    val scope = rememberCoroutineScope()

    fun formIsValid(): Boolean {
        var result = true
        // Username
        if (uiState.username.isEmpty()) {
            uiState = uiState.copy(usernameError = "Username must be provided.")
            result = false
        }
        // Email
        if (uiState.email.isEmpty()) {
            uiState = uiState.copy(emailError = "Email address must be provided.")
            result = false
        } else if (!uiState.email.contains("@")) {
            uiState = uiState.copy(emailError = "Email address must be in correct format.")
            result = false
        }
        // Password
        if (uiState.password.length < 6) {
            uiState = uiState.copy(passwordError = "Password must be 6 or more characters.")
            result = false
        }
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(confirmPasswordError = "Passwords must match.")
            result = false
        }
        return result
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    ) {
        // Message
        Text(
            text = stringResource(R.string.register_message)
        )

        // Image
        Box {
            val imageSelectLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
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
                    .background(colorResource(R.color.grey))
            )
            IconButton(
                onClick = { imageSelectLauncher.launch(
                    input = PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorResource(R.color.grey))
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = "Check mark")
            }
        }

        // Form fields
        TextInputField(
            value = uiState.username,
            onValueChange = { uiState = uiState.copy(username = it, usernameError = null) },
            label = stringResource(R.string.username_label),
            isError = uiState.usernameError != null,
            errorMessage = uiState.usernameError
        )
        TextInputField(
            value = uiState.email,
            onValueChange = { uiState = uiState.copy(email = it, emailError = null) },
            label = stringResource(R.string.email_label),
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError
        )
        PasswordInputField(
            value = uiState.password,
            onValueChange = { uiState = uiState.copy(password = it, passwordError = null) },
            label = stringResource(R.string.password_label),
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError
        )
        PasswordInputField(
            value = uiState.confirmPassword,
            onValueChange = { uiState = uiState.copy(confirmPassword = it, confirmPasswordError = null) },
            label = stringResource(R.string.confirm_password_label),
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError
        )
        Button(
            onClick = {
                if (formIsValid()) {
                    scope.launch {
                        repo.register(
                            uiState.username.trim(),
                            uiState.password,
                            uiState.email.trim(),
                            uiState.imgUri
                        )
                        onRegistrationSuccess()
                    }
                }
            }
        ) {
            Text(stringResource(R.string.register_button))
        }
    }
}
