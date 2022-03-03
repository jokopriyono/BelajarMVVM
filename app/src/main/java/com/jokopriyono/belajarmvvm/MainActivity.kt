package com.jokopriyono.belajarmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jokopriyono.belajarmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, defaultViewModelProviderFactory)
            .get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        binding.btnMinus.setOnClickListener {
            viewModel.decrement()
        }

        binding.btnPlus.setOnClickListener {
            viewModel.increment()
        }

    }
}