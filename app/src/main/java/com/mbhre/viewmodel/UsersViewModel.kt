package com.mbhre.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mbhre.R
import com.mbhre.app.MyApplication
import com.mbhre.model.UsersResponse
import com.mbhre.repository.AppRepository
import com.mbhre.util.Resource
import com.mbhre.util.Utils.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UsersViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val usersData: MutableLiveData<Resource<UsersResponse>> = MutableLiveData()

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch {
        fetchUsers()
    }


    private suspend fun fetchUsers() {
        usersData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication<MyApplication>())) {
                val response = appRepository.getUsers()
                usersData.postValue(handlePicsResponse(response))
            } else {
                usersData.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> usersData.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> usersData.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<UsersResponse>): Resource<UsersResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}