package com.soundbite.packt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentInitialBinding

/**
 * A simple [Fragment] subclass.
 * Use the [InitialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class InitialFragment : Fragment() {

    private lateinit var binding: FragmentInitialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInitialBinding.inflate(layoutInflater)
        val view = binding.root

        binding.initialFragmentBtn.setOnClickListener {
            findNavController().navigate(InitialFragmentDirections.actionInitialFragmentToHomeFragment())
        }

        // Inflate the layout for this fragment
        return view
    }
}