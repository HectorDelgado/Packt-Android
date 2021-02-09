package com.soundbite.packt.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.room.RoomDatabase
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentHomeBinding
import com.soundbite.packt.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        val userDao = db.ownerDogDao()

        Log.d("logz", "Looking through our db...")

        CoroutineScope(Dispatchers.IO).launch {
            //addData(userDao)
            //readData(userDao)
        }

        //ownerDogViewModel.getOwnerAndDogs().
    }

    private suspend fun addData(dao: OwnerDogDao) {
        val ownerUid = Random.nextInt(Int.MAX_VALUE)
        val timestamp = System.currentTimeMillis() / 1000

        val owner = DogOwner(
                ownerUid,
                timestamp,
                "user1001",
                "Madrigal Senegal",
                "Berlin is a great place to visit",
                1,
                1,
                1992
        )

        dao.insertOwner(owner)
        val dogs = listOf(
                Dog(Random.nextInt(Int.MAX_VALUE), 12158, ownerUid, timestamp, "Jeffrey", "a bio", 1, 1, 2018, "Great Dane"),
                Dog(Random.nextInt(Int.MAX_VALUE), 12151, ownerUid, timestamp, "Fluffy", "a bio", 1, 1, 2010, "Poodle"),
                Dog(Random.nextInt(Int.MAX_VALUE), 61211, ownerUid, timestamp, "Phoenix", "a bio", 1, 1, 2011, "Golden Retriever"),
        )
        dao.insertDogs(dogs)
    }

    private suspend fun readData(dao: OwnerDogDao) {
        val ownerWithDogs = dao.getOwnerAndDogs()
        val name = ownerWithDogs.dogOwner.name
        val dogs = ownerWithDogs.dogs.size
        val dogNames = ownerWithDogs.dogs.joinToString { it.name }
        Log.d("logz", "$name has $dogs dogs named $dogNames")
    }

    private fun clearData(db: RoomDatabase) {
        db.clearAllTables()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
//    }
}
