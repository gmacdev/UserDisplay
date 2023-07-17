package com.gmacv.userdisplay.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmacv.userdisplay.data.model.CreateUser
import com.gmacv.userdisplay.data.model.Users
import com.gmacv.userdisplay.data.repository.MainRepository
import com.gmacv.userdisplay.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val namesList = arrayOf("John Doe", "Peter Parker", "Harry Potter", "Bruce Wayne")
    private val jobList = arrayOf("Batman", "Engineer", "Dancer", "Singer")

    private val _usersList = MutableLiveData<List<Users>>()
    val usersList: LiveData<List<Users>>
        get() = _usersList

    private val _createUser = MutableLiveData<CreateUser>()
    val createUser: LiveData<CreateUser>
        get() = _createUser

    private val _networkErrorUserList = MutableLiveData<Boolean>()
    val networkErrorUserList: LiveData<Boolean>
        get() = _networkErrorUserList

    private val _networkErrorCreateUser = MutableLiveData<Boolean>()
    val networkErrorCreateUser: LiveData<Boolean>
        get() = _networkErrorCreateUser

    init {
        loadAllData()
    }

    fun loadAllData() {
        fetchUsers()
    }

    fun addUser() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.createUser(CreateUser(namesList.random(), jobList.random())).let {
                    if (it.isSuccessful)
                        _createUser.postValue(
                            CreateUser(
                                it.body()?.name ?: "Unknown",
                                it.body()?.job ?: "No Job"
                            )
                        )
                    else _networkErrorCreateUser.postValue(true)
                }
            } else _networkErrorCreateUser.postValue(true)
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers().let {
                    if (it.isSuccessful) _usersList.postValue(it.body()?.data ?: arrayListOf())
                    else _networkErrorUserList.postValue(true)
                }
            } else _networkErrorUserList.postValue(true)
        }
    }
}