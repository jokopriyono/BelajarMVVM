package com.jokopriyono.belajarmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jokopriyono.belajarmvvm.data.MainRepository
import com.jokopriyono.belajarmvvm.databinding.ActivityRepoBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RepoActivity : AppCompatActivity(), RepoView {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var viewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = RepoViewModel(MainRepository(this), this)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        binding.btnGetLoad.setOnClickListener {
            GlobalScope.launch {
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