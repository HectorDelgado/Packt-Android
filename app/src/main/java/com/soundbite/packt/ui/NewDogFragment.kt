package com.soundbite.packt.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentNewDogsBinding
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.model.DogBreed
import com.soundbite.packt.model.UserViewModel
import com.soundbite.packt.model.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [NewDogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewDogFragment : Fragment() {
    private val TAG = "T-${javaClass.simpleName}"
    private var _binding: FragmentNewDogsBinding? = null
    private val binding: FragmentNewDogsBinding
        get() = _binding!!

    private val db by lazy {
        UserDatabase.getInstance(requireContext().applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(db.ownerDogDao())
    }
    private val dogBreeds = mutableListOf<DogBreed>()

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val dogBreed = dogBreeds[position].name
            val dogBreedID = dogBreeds[position].id

            // Might have to remove this
            val maxChars = resources.getInteger(R.integer.maxLength_firstName)

            userViewModel.setDogBreed(dogBreed, dogBreedID, maxChars) { result ->
                result.onSuccess {
                }
                result.onFailure { err ->
                    displayMessage(err.message)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewDogsBinding.inflate(inflater, container, false)
        hideForm()

        CoroutineScope(Dispatchers.IO).launch {
            dogBreeds.addAll(userViewModel.getDogBreeds())

            withContext(Dispatchers.Main) {
                setupSpinner()
                showForm()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameET.addTextChangedListener {
            it?.let {
                val dogName = it.toString()
                val maxChars = resources.getInteger(R.integer.maxLength_firstName)

                if (dogName.isNotEmpty()) {
                    userViewModel.setDogName(dogName, maxChars) { result ->
                        result.onSuccess {
                        }
                        result.onFailure { err ->
                            displayMessage(err.message)
                        }
                    }
                }
            }
        }

        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            userViewModel.setDogDateOfBirth(dayOfMonth, month + 1, year) { result ->
                result.onSuccess { date ->
                    binding.dateOfBirthET.setText(date)
                }
                result.onFailure { err ->
                    displayMessage(err.message)
                }
            }
        }

        binding.dateOfBirthET.setOnClickListener {
            Timber.tag(TAG).d("DOB selected")
            DatePickerDialog(requireContext(), dateListener, 2021, 1, 1).show()
        }

        var weightInPounds = true
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            weightInPounds = !isChecked
        }

        binding.weightET.addTextChangedListener {
            it?.let {
                val originalWeight = it.toString()

                if (originalWeight.isNotEmpty()) {
                    val parsedWeight = originalWeight.toDouble()
                    userViewModel.setDogWeight(parsedWeight, weightInPounds) { result ->
                        result.onSuccess {
                        }
                        result.onFailure { err ->
                            displayMessage(err.message)
                        }
                    }
                }
            }
        }

        // setup radio groups
        binding.sexRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.maleRadioBtn -> {
                    userViewModel.setDogSex("Male")
                }
                R.id.femaleRadioBtn -> {
                    userViewModel.setDogSex("Female")
                }
                else -> {
                }
            }
        }

        binding.bioET.addTextChangedListener {
            it?.let {
                val bio = it.toString()
                val maxChars = resources.getInteger(R.integer.maxLength_bio)
                userViewModel.setDogBio(bio, maxChars) { result ->
                    result.onSuccess {
                    }
                    result.onFailure { err ->
                        displayMessage(err.message)
                    }
                }
            }
        }

        binding.doneBtn.setOnClickListener {
            userViewModel.buildDog { result ->
                result.onSuccess {
                    userViewModel.insertDog(it)

                    findNavController().navigate(
                        NewDogFragmentDirections.actionNewDogsFragmentToAllDogsFragment()
                    )
                }
                result.onFailure {
                    Toast.makeText(requireContext(), "Err: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
            // Validate details, grab user data stored locally, store dogs internally ,store all data on server
//            findNavController().navigate(
//                NewDogsFragmentDirections.actionNewDogsFragmentToAllDogsFragment()
//            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun displayMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    private fun setupSpinner() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            dogBreeds.map { it.name }
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.breedSpinner.adapter = adapter
            binding.breedSpinner.onItemSelectedListener = spinnerListener
        }
    }

    private fun hideForm() {
        binding.nestedScrollView.visibility = View.GONE
        binding.semiTransparentView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showForm() {
        binding.nestedScrollView.visibility = View.VISIBLE
        binding.semiTransparentView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }
}
