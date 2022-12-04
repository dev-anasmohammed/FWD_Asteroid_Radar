package com.devanasmohammed.asteroidradar.presentation.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.devanasmohammed.asteroidradar.R
import com.devanasmohammed.asteroidradar.data.AsteroidRepository
import com.devanasmohammed.asteroidradar.data.local.LocalDatabase
import com.devanasmohammed.asteroidradar.data.model.responses.Result
import com.devanasmohammed.asteroidradar.databinding.FragmentMainBinding

/**
1- API call
2- get next 7 days
3- cache next 7 days
------------------------
cached ? getLocal : getRemote
------------------------
work manager:
    -> daily
    -> wifi
    -> charging
    -> download next 7 days and cache
 */

//TODO When asteroids are downloaded, save them in the local database

//TODO Be able to cache the data of the asteroid by using a worker,
// so it downloads and saves today's asteroids in
// background once a day when the device is charging and wifi is enabled..

//TODO Make sure the entire app works without an internet connection.

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        val repository = AsteroidRepository(LocalDatabase.getDatabase(requireContext()))
        val factory = MainViewModelFactory(repository)
        ViewModelProvider(this,factory)[MainViewModel::class.java]
    }

    private val adapter = AsteroidAdapter()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pictureOfDay.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Result.Loading -> {
                    Log.e(tag, "Getting picture of the day from api.....")
                }
                is Result.Success -> {
                    if(response.data?.media_type == "image"){
                        Glide.with(this)
                            .load(response.data.url)
                            .into(binding.activityMainImageOfTheDay)
                    }
                }
                is Result.Error -> {
                }
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Result.Loading -> {
                    Log.e(tag, "Getting picture of the day from api.....")
                }
                is Result.Success -> {
                    val v = response.data
                    adapter.differ.submitList(v)
                }
                is Result.Error -> {
                    viewModel.getLocalAsteroids()
                }
            }
        }

        adapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("asteroid", it)
            }

            findNavController().navigate(
                R.id.action_mainFragment_to_detailFragment,
                bundle
            )
        }
    }

    private fun setupRecyclerView() {
        binding.asteroidRv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
