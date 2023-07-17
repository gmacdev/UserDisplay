package com.gmacv.userdisplay.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gmacv.userdisplay.R
import com.gmacv.userdisplay.data.model.Users
import com.gmacv.userdisplay.databinding.ActivityMainBinding
import com.gmacv.userdisplay.ui.adapter.UserAdapter
import com.gmacv.userdisplay.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActivityMainBinding::bind)
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.usersList.observe(this) {
            if (it.isNullOrEmpty())
                showSnackBarWithAction("No users found")
            else renderListOfUsers(it)
        }
        mainViewModel.networkErrorUserList.observe(this) {
            if (it == true)
                showSnackBarWithAction("Network Error")
        }
        mainViewModel.createUser.observe(this) {
            showSnackBar("User Created: ${it.name}, Job: ${it.job}")
            binding.fab.visibility = View.VISIBLE
        }
        mainViewModel.networkErrorCreateUser.observe(this) {
            if (it == true)
                showSnackBar("Something Went Wrong!")
            binding.fab.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderListOfUsers(users: List<Users>) {
        userAdapter.addData(users)
        userAdapter.notifyDataSetChanged()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)

        userAdapter = UserAdapter(arrayListOf())
        binding.recyclerViewMovies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMovies.adapter = userAdapter

        binding.fab.setOnClickListener { view ->
            mainViewModel.addUser()
            view.visibility = View.GONE
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.main, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackBarWithAction(message: String) {
        val snack = Snackbar.make(binding.main, message, Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.fab)
        snack.setAction("Refresh") {
            mainViewModel.loadAllData()
        }
        snack.show()
    }
}