package com.kanu.loginregister.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanu.loginregister.domain.usecase.RegisterUseCase
import com.kanu.loginregister.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object RegisterSuccess : UiEvent()
    }

    fun onNameChanged(name: String) {
        _state.value = state.value.copy(name = name, nameError = null)
    }

    fun onEmailChanged(email: String) {
        _state.value = state.value.copy(email = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        _state.value = state.value.copy(password = password, passwordError = null)
    }

    fun onConfirmPasswordChanged(confirmPass: String) {
        _state.value = state.value.copy(confirmPassword = confirmPass, confirmPasswordError = null)
    }

    fun togglePasswordVisibility() {
        _state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
    }

    fun register() {
        val name = state.value.name
        val email = state.value.email
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        var hasError = false
        if (name.isBlank()) {
            _state.value = state.value.copy(nameError = "Name cannot be empty")
            hasError = true
        }
        if (email.isBlank()) {
            _state.value = state.value.copy(emailError = "Email cannot be empty")
            hasError = true
        }
        if (password.isBlank()) {
            _state.value = state.value.copy(passwordError = "Password cannot be empty")
            hasError = true
        }
        if (confirmPassword.isBlank()) {
            _state.value = state.value.copy(confirmPasswordError = "Please confirm your password")
            hasError = true
        }
        
        if (hasError) return

        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = registerUseCase(name, email, password, confirmPassword)
            _state.value = state.value.copy(isLoading = false)

            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvent.RegisterSuccess)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {}
            }
        }
    }
}