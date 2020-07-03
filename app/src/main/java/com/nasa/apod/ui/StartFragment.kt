package com.nasa.apod.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.SimpleExoPlayer
import com.nasa.apod.Factory
import com.nasa.apod.R
import com.nasa.apod.data.DataRepository
import com.nasa.apod.databinding.FragmentStartBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private var binding: FragmentStartBinding? = null

    private val viewModel by lazy {
        ViewModelProvider(this, StartViewModelFactory(Factory.dataRepository)).get(StartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)
        binding?.lifecycleOwner = this
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        binding?.pvExo?.player = player
        lifecycle.addObserver(LifecycleAwareExoPlayer(requireContext().applicationContext, player))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            binding?.apod = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


@Suppress("UNCHECKED_CAST")
class StartViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartViewModel(dataRepository) as T
    }
}