package com.lfvp.publickeyappdemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfvp.publickeyappdemo.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewmodel @Inject constructor(authRepository: AuthRepository):ViewModel() {

    var showingSplash by mutableStateOf(true)
        private set


    var isAuthenticated by mutableStateOf<Boolean?>(null)
        private set
    init {
        viewModelScope.launch {
            isAuthenticated=authRepository.isAuthenticated()
            showingSplash=false
        }
    }

}