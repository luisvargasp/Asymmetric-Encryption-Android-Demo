package com.lfvp.publickeyappdemo.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel  @Inject constructor(val authUseCase: AuthUseCase):ViewModel() {
    init {

        Log.d("TrackLaunched","InitViewmodel")

    }
    var isLoading by mutableStateOf(false)
        private set

/*
    private val _showError =
        MutableStateFlow<String?>(null)
    val showError: StateFlow<String?> = _showError

    private val _authSuccess =
        MutableStateFlow<Boolean?>(null)
    val authSuccess: StateFlow<Boolean?> = _authSuccess*/
     var showError by mutableStateOf<String?>(null)
    private set

    var authSuccess by mutableStateOf<Boolean?>(null)
        private set


    var isLogin by mutableStateOf(true)
        private set

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set



    fun updateEmail(value: String) {
        email = value
    }

    fun updatePassword(value: String) {
        password = value
    }
    fun toggleAuth(){
        isLogin=isLogin.not()
    }
    fun resetOneTimeEvents(){
        showError=null
        authSuccess=null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TrackLaunched","onDestroyViewmodel")

    }
     fun performAction(){
         isLoading=true

         if(isLogin){
             viewModelScope.launch(Dispatchers.IO) {
                 delay(3000)


                 try {
                     val result= authUseCase.login(email,password)
                     if(result){
                         authSuccess=true
                     }

                 }catch (e:Exception){
                     showError=e.message

                 }
                 isLoading=false
             }

         }else{
             viewModelScope.launch(Dispatchers.IO) {
                 delay(3000)

                 try {
                     val result= authUseCase.signUp(email,password)
                     if(result){
                         authSuccess=true
                     }

                 }catch (e:Exception){
                     showError=e.message

                 }
                 isLoading=false

             }

         }

    }


}