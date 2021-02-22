package com.soundbite.packt.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.MockDataCreator
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentHomeBinding
import com.soundbite.packt.db.OwnerDogViewModel
import com.soundbite.packt.db.OwnerDogViewModelFactory
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.network.RemoteDataBaseViewModelFactory
import com.soundbite.packt.network.RemoteDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val db by lazy {
        UserDatabase.getInstance(requireContext().applicationContext)
    }
    private val ownerDogViewModel: OwnerDogViewModel by viewModels {
        OwnerDogViewModelFactory(db.ownerDogDao())
    }
    private val remoteDatabaseViewModel: RemoteDatabaseViewModel by viewModels {
        RemoteDataBaseViewModelFactory()
    }
    private val firebaseDataBase by lazy {
        Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        // (requireActivity() as DrawerLocker).setDrawerEnabled(true)

        val userDao = db.ownerDogDao()

        Log.d("logz", "Looking through our db...")

        CoroutineScope(Dispatchers.IO).launch {
            // clearData()
            // addData()
            // readData()
             //newUser()
            val timeStamp = System.currentTimeMillis() / 1000
            val currentUser = FirebaseAuth.getInstance().currentUser!!
            val mockData = MockDataCreator(currentUser.uid, timeStamp)
            val dogs = mockData.dogs
            val dogOwner = mockData.dogOwner

            remoteDatabaseViewModel.addNewDataToServer("users", dogOwner.ownerUid, dogOwner) { isSuccess ->
                Log.d("logz", "Wrote to server: $isSuccess")
            }
            dogs.forEach { dog ->
                remoteDatabaseViewModel.addNewDataToServer("dogs", dog.dogUid, dog) { isSuccess ->
                    Log.d("logz", "Wrote dog to server: $isSuccess")
                }
            }
        }
    }

    private fun newUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser!!



        firebaseDataBase
            .child("users")
            .child(currentUser.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.d("logz", "User exist")
                        } else {
                            Log.d("logz", "user is new")

                            CoroutineScope(Dispatchers.IO).launch {

                                ownerDogViewModel.getDogOwner().singleOrNull()?.let { dogOwner ->
                                    ownerDogViewModel.getDogs().singleOrNull()?.let { dogs ->
                                        firebaseDataBase
                                            .child("users")
                                            .child(currentUser.uid)
                                            .setValue(dogOwner)
                                        dogs.forEach {
                                            firebaseDataBase
                                                .child("dogs")
                                                .child(it.dogUid)
                                                .setValue(it)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
    }

    private suspend fun addData() {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            val timeStamp = System.currentTimeMillis() / 1000
            val userUid = firebaseUser.uid
            val mockData = MockDataCreator(userUid, timeStamp)

            val dogs = mockData.dogs
            val dogOwner = mockData.dogOwner

            ownerDogViewModel.insertDogOwnerAndDogs(dogOwner, dogs)
        }
    }

    private suspend fun readData() {
        ownerDogViewModel.getDogOwner().singleOrNull()?.let { dogOwner ->
            ownerDogViewModel.getDogs().singleOrNull()?.let { dogs ->
                Log.d(
                    "logz",
                    "${dogOwner.name} has ${dogs.size} dogs named ${dogs.map { it.name }}"
                )
            }
        }
    }

    private fun clearData() {
        db.clearAllTables()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }
}
