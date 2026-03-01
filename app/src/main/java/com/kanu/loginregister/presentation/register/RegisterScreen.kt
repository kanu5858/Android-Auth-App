package com.kanu.loginregister.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kanu.loginregister.presentation.components.AuthTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RegisterViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is RegisterViewModel.UiEvent.RegisterSuccess -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = "Full Name",
                leadingIcon = Icons.Default.Person,
                isError = state.nameError != null,
                errorMessage = state.nameError
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                leadingIcon = Icons.Default.Email,
                isError = state.emailError != null,
                errorMessage = state.emailError
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                isError = state.passwordError != null,
                errorMessage = state.passwordError
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                label = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = state.confirmPasswordError != null,
                errorMessage = state.confirmPasswordError
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::register,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Register")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? Login")
            }
        }
    }
}