package com.nasa.apod.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
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
import com.nasa.apod.utils.dateFormatter
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment(), DatePickerDialog.OnDateSetListener {

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
        //lifecycle.addObserver(LifecycleAwareExoPlayer(requireContext().applicationContext, player))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivCalendar?.setOnClickListener {
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        viewModel.state.observe(viewLifecycleOwner, Observer {
            binding?.apod = it
            Picasso.get().load(it.hdUrl).into(binding?.ivApod)
            binding?.executePendingBindings()
        })

    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        viewModel.fetchPhoto(dateFormatter.format(calendar.time))
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