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
import androidx.room.RoomDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentHomeBinding
import com.soundbite.packt.db.Dog
import com.soundbite.packt.db.DogOwner
import com.soundbite.packt.db.OwnerDogViewModel
import com.soundbite.packt.db.OwnerDogViewModelFactory
import com.soundbite.packt.db.UserDatabase
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            addData()
            // readData()
        }
    }

    private suspend fun addData() {
        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
            val timeStamp = System.currentTimeMillis() / 1000
            val userUid = firebaseUser.uid

            val dogs = listOf(
                Dog(
                    UUID.randomUUID().toString(),
                    userUid,
                    12158,
                    timeStamp,
                    "Jeffrey",
                    "a bio",
                    1,
                    1,
                    2018,
                    "Great Dane"
                ),
                Dog(
                    UUID.randomUUID().toString(),
                    userUid,
                    12151,
                    timeStamp,
                    "Fluffy",
                    "a bio",
                    1,
                    1,
                    2010,
                    "Poodle"
                ),
                Dog(
                    UUID.randomUUID().toString(),
                    userUid,
                    61211,
                    timeStamp,
                    "Phoenix",
                    "a bio",
                    1,
                    1,
                    2011,
                    "Golden Retriever"
                ),
            )

            val dogIds = dogs.map { it.dogUid }

            val owner = DogOwner(
                firebaseUser.uid,
                dogIds,
                timeStamp,
                "TrashUser1034",
                firebaseUser.displayName ?: "default name",
                "Berlin is a great place to visit anytime.",
                6,
                3,
                1992,
            )

            ownerDogViewModel.insertDogOwnerAndDogs(owner, dogs)
        }
    }

    private suspend fun readData() {
    }

    private fun clearData(db: RoomDatabase) {
        db.clearAllTables()
    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // val post = snapshot.getValue<OwnerWithDogs>()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
//    }
}
