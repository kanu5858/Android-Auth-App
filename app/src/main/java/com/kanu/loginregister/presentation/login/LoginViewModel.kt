package com.kanu.loginregister.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanu.loginregister.domain.usecase.LoginUseCase
import com.kanu.loginregister.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object LoginSuccess : UiEvent()
    }

    fun onEmailChanged(email: String) {
        _state.value = state.value.copy(email = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        _state.value = state.value.copy(password = password, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
    }

    fun login() {
        val email = state.value.email
        val password = state.value.password

        if (email.isBlank()) {
            _state.value = state.value.copy(emailError = "Email cannot be empty")
            return
        }
        if (password.isBlank()) {
            _state.value = state.value.copy(passwordError = "Password cannot be empty")
            return
        }

        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = loginUseCase(email, password)
            _state.value = state.value.copy(isLoading = false)

            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvent.LoginSuccess)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {}
            }
        }
    }
}