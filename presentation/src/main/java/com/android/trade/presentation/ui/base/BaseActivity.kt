package com.android.trade.presentation.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>(
    private val inflateBinding: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun setupView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    fun navigateActivity(destination: Class<out Activity>) {
        startActivity(Intent(this, destination))
    }
}
