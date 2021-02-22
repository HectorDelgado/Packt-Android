package com.soundbite.packt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
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
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val TAG = "T-${javaClass.simpleName}"

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
        Timber.tag(TAG).d("onViewCreated")

        CoroutineScope(Dispatchers.IO).launch {
            // clearData()
            // addData()
            // readData()
            // newUser()
            val timeStamp = System.currentTimeMillis() / 1000
            val currentUser = FirebaseAuth.getInstance().currentUser!!
            val mockData = MockDataCreator(currentUser.uid, timeStamp)
            val dogs = mockData.dogs
            val dogOwner = mockData.dogOwner
        }
    }

    private fun clearData() {
        db.clearAllTables()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }
}
