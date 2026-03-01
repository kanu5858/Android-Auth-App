package com.kanu.loginregister.di

import android.content.Context
import androidx.room.Room
import com.kanu.loginregister.data.local.AppDatabase
import com.kanu.loginregister.data.local.TokenManager
import com.kanu.loginregister.data.local.UserDao
import com.kanu.loginregister.data.remote.AuthApi
import com.kanu.loginregister.data.remote.MockInterceptor
import com.kanu.loginregister.data.repository.AuthRepositoryImpl
import com.kanu.loginregister.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(AuthApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        tokenManager: TokenManager,
        userDao: UserDao
    ): AuthRepository = AuthRepositoryImpl(api, tokenManager, userDao)
}