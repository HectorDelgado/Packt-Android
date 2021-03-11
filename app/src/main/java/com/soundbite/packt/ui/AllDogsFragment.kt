package com.soundbite.packt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.soundbite.packt.ui.AllDogsFragmentDirections
import com.soundbite.packt.adapter.DogBriefAdapter
import com.soundbite.packt.databinding.FragmentAllDogsBinding
import com.soundbite.packt.db.DogOwner
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.model.DogBriefItem
import com.soundbite.packt.model.UserViewModel
import com.soundbite.packt.model.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [AllDogsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllDogsFragment : Fragment() {
    private val TAG = "T-${javaClass.simpleName}"
    private var _binding: FragmentAllDogsBinding? = null
    private val binding
        get() = _binding!!
    private val db by lazy {
        UserDatabase.getInstance(requireContext().applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(db.ownerDogDao())
    }

    private lateinit var dogBriefAdapter: DogBriefAdapter
    private val dogBriefItems = mutableListOf<DogBriefItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllDogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            userViewModel.getDogs().single().forEach {
                dogBriefItems.add(DogBriefItem(it.name, it.breed))
            }

            withContext(Dispatchers.Main) {
                dogBriefAdapter = DogBriefAdapter((dogBriefItems))
                binding.dogsRV.apply {
                    adapter = dogBriefAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }

        binding.addDogBtn.setOnClickListener {
            findNavController().navigate(
                AllDogsFragmentDirections.actionAllDogsFragmentToNewDogsFragment()
            )
        }

        binding.doneBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val dogOwner = userViewModel.getDogOwner().single()
                val dogs = userViewModel.getDogs().single()

                userViewModel.clearDatabase {
                    val user = DogOwner(
                        userViewModel.currentUser!!.uid,
                        dogOwner.username,
                        dogOwner.firstName,
                        dogOwner.lastName,
                        dogOwner.bio,
                        dogOwner.birthDay,
                        dogOwner.birthMonth,
                        dogOwner.birthYear,
                        dogs.map { it.uid },
                        dogOwner.created,
                        dogOwner.lastLogin,
                        dogOwner.img
                    )
                    Timber.tag(TAG).d("Inserting new data!")
                    userViewModel.insertDogOwner(user)
                    userViewModel.insertDogs(dogs)
                    userViewModel.addNewDataToRemoteDatabase(UserViewModel.DATABASE_PATH_USERS, user.uid, user) { isSuccess ->
                        if (isSuccess) {
                            Timber.tag(TAG).d("User wrote to firebase.")
                            dogs.forEach { dog ->
                                userViewModel.addNewDataToRemoteDatabase(UserViewModel.DATABASE_PATH_DOGS, dog.uid, dog) { isSuccess ->
                                    Timber.tag(TAG).d("Dog wrote to firebase.")
                                }
                            }

                            Timber.tag(TAG).d("Writing complete! leaving now!.")
                            findNavController().navigate(
                                AllDogsFragmentDirections.actionAllDogsFragmentToHomeFragment()
                            )
                        }
                    }
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