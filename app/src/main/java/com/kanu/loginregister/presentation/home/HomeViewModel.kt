package com.kanu.loginregister.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanu.loginregister.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object LogoutSuccess : UiEvent()
    }

    init {
        repository.getAuthenticatedUser().onEach { user ->
            _state.value = state.value.copy(user = user)
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _eventFlow.emit(UiEvent.LogoutSuccess)
        }
    }
}

data class HomeState(
    val user: com.kanu.loginregister.domain.model.User? = null
)