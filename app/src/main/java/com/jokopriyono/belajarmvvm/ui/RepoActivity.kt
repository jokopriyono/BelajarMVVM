package com.jokopriyono.belajarmvvm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jokopriyono.belajarmvvm.databinding.ActivityRepoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private val viewModel: RepoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepoBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@RepoActivity
            vm = viewModel
        }
        setContentView(binding.root)

        binding.btnGetLoad.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchAndLoadCharacters()
            }
        }
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.loading.observe(this) {
            binding.loading.visibility =
                if (it) View.VISIBLE
                else View.GONE
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

}