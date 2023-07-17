package com.gmacv.userdisplay.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmacv.userdisplay.data.model.UserCredentials
import com.gmacv.userdisplay.data.repository.MainRepository
import com.gmacv.userdisplay.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean>
        get() = _login

    private val _networkErrorUserLogin = MutableLiveData<String>()
    val networkErrorUserLogin: LiveData<String>
        get() = _networkErrorUserLogin

    private val _register = MutableLiveData<Boolean>()
    val register: LiveData<Boolean>
        get() = _register

    private val _networkErrorUserRegister = MutableLiveData<String>()
    val networkErrorUserRegister: LiveData<String>
        get() = _networkErrorUserRegister


    fun loginUser(credentials: UserCredentials) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.loginUser(credentials).let {
                    if (it.isSuccessful && !it.body()?.token.isNullOrBlank())
                        _login.postValue(true)
                    else _networkErrorUserLogin.postValue(
                        it.errorBody()?.string() ?: "Use Registered Users"
                    )
                }
            } else _networkErrorUserLogin.postValue("Network Error on Login")
        }
    }

    fun registerUser(credentials: UserCredentials) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.registerUser(credentials).let {
                    if (it.isSuccessful && !it.body()?.token.isNullOrBlank())
                        _register.postValue(true)
                    else _networkErrorUserRegister.postValue(
                        it.errorBody()?.string() ?: "Use Registered Users"
                    )
                }
            } else _networkErrorUserRegister.postValue("Network Error on Register")
        }
    }
}