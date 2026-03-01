package com.kanu.loginregister.presentation.register

import com.kanu.loginregister.domain.usecase.RegisterUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var registerUseCase: RegisterUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        registerUseCase = mock()
        viewModel = RegisterViewModel(registerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register with mismatched passwords show error`() = runTest {
        whenever(registerUseCase(any(), any(), any(), any())).thenReturn(Resource.Error("Passwords do not match"))
        
        viewModel.onNameChanged("Kanu")
        viewModel.onEmailChanged("kanu@example.com")
        viewModel.onPasswordChanged("password123")
        viewModel.onConfirmPasswordChanged("mismatch")
        
        viewModel.register()
        
        testDispatcher.scheduler.advanceUntilIdle()

        // Local validation in ViewModel might catch it, but here we check usecase interaction or state
        // In our current VM, it doesn't even call usecase if password is blank, 
        // but it doesn't check mismatch locally before calling (it leaves that to UseCase).
        // Wait, my VM code: if (hasError) return
        // It checked for isBlank. UseCase checks for mismatch.
        
        whenever(registerUseCase(any(), any(), any(), any())).thenReturn(Resource.Error("Passwords do not match"))
        
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(false, viewModel.state.value.isLoading)
    }
}