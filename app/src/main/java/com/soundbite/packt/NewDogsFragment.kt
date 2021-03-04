package com.soundbite.packt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soundbite.packt.databinding.FragmentNewDogsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewDogsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewDogsFragment : Fragment() {
    private var _binding: FragmentNewDogsBinding? = null
    private val binding: FragmentNewDogsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewDogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dontBtn.setOnClickListener {
            findNavController().navigate(
                NewDogsFragmentDirections.actionNewDogsFragmentToHomeFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
