package com.mbhre.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mbhre.adapter.UsersAdapter
import com.mbhre.databinding.ActivityMainBinding
import com.mbhre.repository.AppRepository
import com.mbhre.util.*
import com.mbhre.viewmodel.UsersViewModel
import com.mbhre.viewmodel.ViewModelProviderFactory
import com.mbhre.util.Resource

/**
 * List screen data access functionality
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UsersViewModel
    lateinit var userAdapter: UsersAdapter
    lateinit var MainBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = MainBinding.root
        setContentView(view)
        showProgressBar()
        init()
    }

    /**
     * create an instance for views and adapters
     *
     */
    private fun init() {
        MainBinding.rvPics.setHasFixedSize(true)
        MainBinding.rvPics.layoutManager = LinearLayoutManager(this)
        userAdapter = UsersAdapter(applicationContext)
        setupViewModel()
    }

    /**
     * view-model instance with activity
     *
     */
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UsersViewModel::class.java)
        getUsers()
    }

    /**
     * get list of data from api with view-model
     *
     */
    private fun getUsers() {
        viewModel.usersData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { usersResponse ->
                        userAdapter.differ.submitList(usersResponse)
                        MainBinding.rvPics.adapter = userAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        MainBinding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //showProgressBar()
                }
            }
        })
    }

    /**
     * hide progressbar after success from api call
     *
     */
    private fun hideProgressBar() {
        MainBinding.progress.visibility = View.GONE
    }

    /**
     * show progressbar after submit button click
     *
     */
    private fun showProgressBar() {
        MainBinding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    /**
     * back button functionality of app
     *
     */
    override fun onBackPressed() {
        super.onBackPressed()

        Intent(this@MainActivity, LoginActivity::class.java).also {
            startActivity(it)
        }
    }
}