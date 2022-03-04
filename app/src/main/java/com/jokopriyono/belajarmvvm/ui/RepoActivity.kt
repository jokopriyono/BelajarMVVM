package com.jokopriyono.belajarmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jokopriyono.belajarmvvm.MyViewModelFactory
import com.jokopriyono.belajarmvvm.data.MainRepository
import com.jokopriyono.belajarmvvm.databinding.ActivityRepoBinding
import kotlinx.coroutines.*

class RepoActivity : AppCompatActivity(), RepoView {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var viewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = MyViewModelFactory(MainRepository(this))
        viewModel = ViewModelProvider(this, factory).get(RepoViewModel::class.java)
            .apply { view = this@RepoActivity }

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
    }

    override fun showLoading() {
        runOnUiThread {
            binding.loading.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        runOnUiThread {
            binding.loading.visibility = View.GONE
        }
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}