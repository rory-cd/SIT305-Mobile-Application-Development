package com.rorycd.learningassistant.ui.register

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.ui.components.PasswordInputField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.TextInputField

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
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val failMsg = stringResource(R.string.username_taken)
    val context = LocalContext.current

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
                    viewModel.onImageSelected(uri.toString())
                }
            }
            AsyncImage(
                model = state.imgUri,
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
            value = state.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            label = stringResource(R.string.username_label),
            isError = state.usernameError != null,
            errorMessage = state.usernameError?.let { stringResource(it) } ?: "",
            modifier = Modifier
                .padding(top = 24.dp)
        )
        TextInputField(
            value = state.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = stringResource(R.string.email_label),
            isError = state.emailError != null,
            errorMessage = state.emailError?.let { stringResource(it) } ?: ""
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
