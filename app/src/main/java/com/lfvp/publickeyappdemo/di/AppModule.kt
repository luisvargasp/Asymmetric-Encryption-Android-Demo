package com.lfvp.publickeyappdemo.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.lfvp.publickeyappdemo.data.AndroidKeyStoreRepository
import com.lfvp.publickeyappdemo.data.AuthRepository
import com.lfvp.publickeyappdemo.data.FirebaseAuthRepository
import com.lfvp.publickeyappdemo.data.FirebaseDatabaseRepository
import com.lfvp.publickeyappdemo.data.KeyRepository
import com.lfvp.publickeyappdemo.data.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth()=FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideFirebaseDatabase()=FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuthRepository(firebaseAuth: FirebaseAuth):AuthRepository{
        return FirebaseAuthRepository(firebaseAuth)

    }
    @Singleton
    @Provides
    fun provideFirebaseDatabaseRepository(authRepository: AuthRepository,firebaseDatabase: FirebaseDatabase,keyRepository: KeyRepository):RemoteRepository{
        return FirebaseDatabaseRepository(authRepository,firebaseDatabase,keyRepository)
    }
    @Singleton
    @Provides
    fun provideAndroidKeyStoreRepository():KeyRepository=
         AndroidKeyStoreRepository()


}