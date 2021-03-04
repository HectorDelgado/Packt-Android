package com.soundbite.packt.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentNewUserBinding
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.model.UserViewModel
import com.soundbite.packt.model.UserViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [NewUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewUserFragment : Fragment() {
    private val binding
        get() = _binding!!
    private val db by lazy {
        UserDatabase.getInstance(requireContext().applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(db.ownerDogDao())
    }
    private val TAG = "T-${javaClass.simpleName}"

    private var _binding: FragmentNewUserBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstNameET.addTextChangedListener {
            it?.let {
                val firstName = it.toString()
                val maxLength = resources.getInteger(R.integer.maxLength_firstName)

                userViewModel.setFirstName(firstName, maxLength) { result ->
                    result.onSuccess {
                        binding.firstNameWarningTV.visibility = View.GONE
                    }
                    result.onFailure { err ->
                        binding.firstNameWarningTV.visibility = View.VISIBLE
                        displayMessage(err.message)
                    }
                }
            }
        }

        binding.lastNameET.addTextChangedListener {
            it?.let {
                val lastName = it.toString()
                val maxLength = resources.getInteger(R.integer.maxLength_lastName)

                userViewModel.setLastName(lastName, maxLength) { result ->
                    result.onSuccess {
                        binding.lastNameWarningTV.visibility = View.GONE
                    }
                    result.onFailure { err ->
                        binding.lastNameWarningTV.visibility = View.VISIBLE
                        displayMessage(err.message)
                    }
                }
            }
        }

        binding.userNameET.addTextChangedListener {
            it?.let {
                val userName = it.toString()
                val maxLength = resources.getInteger(R.integer.maxLength_userName)

                userViewModel.setUsername(userName, maxLength) { result ->
                    result.onSuccess {
                        binding.userNameWarningTV.visibility = View.GONE
                    }
                    result.onFailure { err ->
                        binding.userNameWarningTV.visibility = View.VISIBLE
                        displayMessage(err.message)
                    }
                }
            }
        }

        binding.bioET.addTextChangedListener {
            it?.let {
                val bio = it.toString()
                val maxLength = resources.getInteger(R.integer.maxLength_bio)
                val characterCount = "${bio.length}/255"

                binding.bioLengthTV.text = characterCount

                userViewModel.setBio(bio, maxLength) { result ->
                    result.onSuccess {
                        binding.bioWarningTV.visibility = View.GONE
                    }
                    result.onFailure { err ->
                        binding.bioWarningTV.visibility = View.VISIBLE
                        displayMessage(err.message)
                    }
                }
            }
        }

        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            userViewModel.setDateOfBirth(dayOfMonth, month + 1, year) { result ->
                result.onSuccess { date ->
                    binding.dateOfBirthWarningTV.visibility = View.GONE
                    binding.dateOfBirthET.setText(date)
                }
                result.onFailure { err ->
                    binding.dateOfBirthWarningTV.visibility = View.VISIBLE
                    displayMessage(err.message)
                }
            }
        }

        binding.dateOfBirthET.setOnClickListener {
            DatePickerDialog(requireContext(), dateListener, 2021, 1, 1).show()
        }

        binding.nextPageBtn.setOnClickListener {
            userViewModel.buildUser { result ->
                result.onSuccess { user ->
                    userViewModel.insertDogOwner(user)

                    findNavController().navigate(
                        NewUserFragmentDirections.actionNewUserFragmentToNewDogsFragment()
                    )
                }
                result.onFailure { err ->
                    displayMessage(err.message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
}
