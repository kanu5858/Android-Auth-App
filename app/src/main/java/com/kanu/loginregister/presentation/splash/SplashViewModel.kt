package com.kanu.loginregister.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanu.loginregister.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object NavigateToHome : UiEvent()
        object NavigateToAuth : UiEvent()
    }

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val token = tokenManager.token.first()
            if (token != null) {
                _eventFlow.emit(UiEvent.NavigateToHome)
            } else {
                _eventFlow.emit(UiEvent.NavigateToAuth)
            }
        }
    }
}