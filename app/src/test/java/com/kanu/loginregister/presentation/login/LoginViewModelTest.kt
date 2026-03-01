package com.kanu.loginregister.presentation.login

import com.kanu.loginregister.domain.model.User
import com.kanu.loginregister.domain.usecase.LoginUseCase
import com.kanu.loginregister.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginUseCase: LoginUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mock()
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials updates state to success`() = runTest {
        val user = User("1", "Kanu", "kanu@example.com", "token")
        whenever(loginUseCase("kanu@example.com", "password")).thenReturn(Resource.Success(user))

        viewModel.onEmailChanged("kanu@example.com")
        viewModel.onPasswordChanged("password")
        viewModel.login()
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.state.value.isLoading)
    }
}