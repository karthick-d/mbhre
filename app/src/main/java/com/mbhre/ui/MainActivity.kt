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

    private fun init() {
        MainBinding.rvPics.setHasFixedSize(true)
        MainBinding.rvPics.layoutManager = LinearLayoutManager(this)
        userAdapter = UsersAdapter(applicationContext)
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UsersViewModel::class.java)
        getUsers()
    }

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

    private fun hideProgressBar() {
        MainBinding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        MainBinding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Intent(this@MainActivity, LoginActivity::class.java).also {
            startActivity(it)
        }
    }
}