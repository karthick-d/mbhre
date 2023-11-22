package com.mbhre.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mbhre.R
import com.mbhre.app.MyApplication
import com.mbhre.model.loginResponse.LoginResponse
import com.mbhre.network.RequestBodies
import com.mbhre.repository.AppRepository
import com.mbhre.util.Event
import com.mbhre.util.Resource
import com.mbhre.util.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * class represent for access data and business logic for create screen
 *
 * @property appRepository
 * @constructor
 * TODO
 *
 * @param app
 */
class LoginViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResponse:LiveData<Event<Resource<LoginResponse>>> = _loginResponse

    /**
     * input parse data access for user screen
     *
     * @param body
     */
    fun loginUser(body: RequestBodies.LoginBody) = viewModelScope.launch {
        login(body)
    }

    /**
     * method represent intermediate data pass to repository and handle output response
     *
     * @param body
     */
    private suspend fun login(body: RequestBodies.LoginBody) {
        _loginResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = appRepository.loginUser(body)
                _loginResponse.postValue(handleUserResponse(response))
            } else {
                _loginResponse.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection))))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _loginResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error
                            )
                        ))
                    )
                }
            }
        }
    }

    /**
     * api call output response success or failure methods
     *
     * @param response
     * @return
     */
    private fun handleUserResponse(response: Response<LoginResponse>): Event<Resource<LoginResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}