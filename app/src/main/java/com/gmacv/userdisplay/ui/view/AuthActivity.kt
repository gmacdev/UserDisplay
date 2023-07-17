package com.gmacv.userdisplay.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gmacv.userdisplay.R
import com.gmacv.userdisplay.data.model.UserCredentials
import com.gmacv.userdisplay.databinding.ActivityAuthBinding
import com.gmacv.userdisplay.ui.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    private val authViewModel: AuthViewModel by viewModels()
    private val binding by viewBinding(ActivityAuthBinding::bind)
    private var isRegister = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        binding.switchButton.setOnClickListener {
            isRegister = !isRegister
            when (isRegister) {
                true -> {
                    binding.toolbar.title = getString(R.string.register)
                    binding.authButton.text = getString(R.string.register)
                    binding.switchButton.text = getString(R.string.already_a_user_login)
                }

                false -> {
                    binding.toolbar.title = getString(R.string.login)
                    binding.authButton.text = getString(R.string.login)
                    binding.switchButton.text = getString(R.string.not_a_user_register)
                }
            }
        }
        binding.authButton.setOnClickListener {
            hideKeyboard()
            if (isValidated()) {
                showSnackBar(getString(R.string.checking_credentials))
                val userCredentials = UserCredentials(
                    binding.emailET.text.toString().trim(),
                    binding.passwordET.text.toString().trim()
                )
                when (isRegister) {
                    true -> authViewModel.registerUser(userCredentials)
                    false -> authViewModel.loginUser(userCredentials)
                }
            }
        }
        binding.autoFillButton.setOnClickListener {
            binding.emailET.setText(getString(R.string.demo_email))
            binding.passwordET.setText(getString(R.string.demo_password))
        }
    }

    private fun isValidated(): Boolean {
        if (binding.emailET.text.isEmpty()) {
            showSnackBar(getString(R.string.enter_valid_email))
            return false
        }
        if (binding.passwordET.text.isEmpty()) {
            showSnackBar(getString(R.string.enter_password))
            return false
        }

        var isValid = true
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(binding.emailET.text.toString().trim())
        if (!matcher.matches()) {
            isValid = false
            showSnackBar(getString(R.string.enter_valid_email))
        }
        return isValid
    }

    private fun setupObserver() {
        authViewModel.login.observe(this) {
            if (it == false)
                showSnackBar("Unable to Login")
            else moveToMainActivity()
        }
        authViewModel.networkErrorUserLogin.observe(this) {
            showSnackBar(it.toString())
        }
        authViewModel.register.observe(this) {
            if (it == false)
                showSnackBar("Unable to Register")
            else moveToMainActivity()
        }
        authViewModel.networkErrorUserRegister.observe(this) {
            showSnackBar(it.toString())
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        finish()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.main, message, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.switchButton).show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            binding.authButton.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }
}