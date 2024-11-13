package com.lfvp.publickeyappdemo.screens.auth

import com.lfvp.publickeyappdemo.data.AuthRepository
import com.lfvp.publickeyappdemo.data.KeyRepository
import com.lfvp.publickeyappdemo.data.RemoteRepository
import com.lfvp.publickeyappdemo.models.UserInfo
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AuthUseCase @Inject constructor(
    val authRepository: AuthRepository,
    val remoteRepository: RemoteRepository,
    val keyRepository: KeyRepository
) {


    suspend fun login(email: String, password: String): Boolean {
        val loginResult = authRepository.login(email, password)

        if (loginResult) {
            if(!keyRepository.existKey(authRepository.getUserId())){
                keyRepository.createKey(authRepository.getUserId())
                val result = remoteRepository.updatePublicKey(keyRepository.getPublicKey(authRepository.getUserId()))
                return result
            }
            return true
        } else {
            return false

        }

    }

    suspend fun signUp(email: String, password: String): Boolean {
        val signUpResult = authRepository.signUp(email, password)

        if (signUpResult) {
            //if(!keyRepository.existKey(email)){
                keyRepository.createKey(authRepository.getUserId())
                val result = remoteRepository.createUserInDB(UserInfo(email=email, publicKey = keyRepository.getPublicKey(authRepository.getUserId())))
                return result

          //  }

        } else {
            return false

        }

    }
}