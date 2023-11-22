package com.mbhre.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mbhre.R
import com.mbhre.databinding.ActivityLoginBinding
import com.mbhre.network.RequestBodies
import com.mbhre.repository.AppRepository
import com.mbhre.util.Resource
import com.mbhre.util.errorSnack
import com.mbhre.viewmodel.LoginViewModel
import com.mbhre.viewmodel.ViewModelProviderFactory

/**
 * class represent main screen data operations
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = activityLoginBinding.root
        setContentView(view)
        //
        init()
    }

    /**
     * create an instance for repository and view-model class
     *
     */
    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    /**
     * submit button click functionality and api calls
     *
     * @param view
     */
    fun onLoginClick(view: View) {
        var email = activityLoginBinding.edtEmail.text.toString()
        val username = activityLoginBinding.edtUsername.text.toString()
        val gender = activityLoginBinding.spinnerForGender.selectedItem.toString()
        val status = activityLoginBinding.spinnerForStatus.selectedItem.toString()



        if (email.isNotEmpty() && username.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                email
            )
                .matches() && !gender.equals("----select your gender---") && !status.equals("----select your status---")
        ) {
            val body = RequestBodies.LoginBody(
                username,
                gender,
                email,
                status
            )

            loginViewModel.loginUser(body)
            loginViewModel.loginResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { loginResponse ->
                                Intent(this@LoginActivity, MainActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                activityLoginBinding.progress.errorSnack(
                                    "Server Error or Emailid already Exists",
                                    Snackbar.LENGTH_LONG
                                )
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            })
        } else {
            if (username.isEmpty()) {
                activityLoginBinding.edtUsername.error = getString(R.string.username_mandatory)
            } else if (activityLoginBinding.edtEmail.text.toString()
                    .equals("")
            ) {
                activityLoginBinding.edtEmail.error = (getString(R.string.emailid_mandatory))

            } else
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        activityLoginBinding.edtEmail.text.toString()
                    ).matches()
                ) {

                    activityLoginBinding.edtEmail.error = (getString(R.string.valid_emailid))
                } else if (gender.equals("----select your gender---")) {
                    val errorText = activityLoginBinding.spinnerForGender.selectedView as TextView
                    errorText.error = getString(R.string.gender_mandatory)
                    errorText.requestFocus()

                } else if (status.equals("----select your status---")) {
                    val errorText = activityLoginBinding.spinnerForStatus.selectedView as TextView
                    errorText.error = getString(R.string.status_mandatory)
                    errorText.requestFocus()

                }
        }
    }

    /**
     * hide progressbar after success from api call
     *
     */
    private fun hideProgressBar() {
        activityLoginBinding.progress.visibility = View.GONE
    }

    /**
     * show progressbar after submit button click
     *
     */
    private fun showProgressBar() {
        activityLoginBinding.progress.visibility = View.VISIBLE
    }


}