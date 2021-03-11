package com.soundbite.packt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentHomeBinding
import com.soundbite.packt.db.DogOwner
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.model.UserViewModel
import com.soundbite.packt.model.UserViewModelFactory
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {
    private val TAG = "T-${javaClass.simpleName}"
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val db by lazy {
        UserDatabase.getInstance(requireContext().applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(db.ownerDogDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        // TODO Current user is not updated internally or new user is not detected!
        lifecycleScope.launch {
            userViewModel.currentUser?.let { fbUser ->
                Timber.tag(TAG).d("User UID is ${fbUser.uid}")
                if (userViewModel.isNewUser(fbUser.uid)) {
                    Timber.tag(TAG).d("User not found in remote database")
                    findNavController()
                        .navigate(
                            HomeFragmentDirections.actionHomeFragmentToNewUserFragment()
                        )
                } else {
                    Timber.tag(TAG).d("User found in remote database")
                    val user = userViewModel.readDataFromDatabase(UserViewModel.DATABASE_PATH_USERS, fbUser.uid, DogOwner::class.java)
                    binding.tempTV.text = "Welcome to Packt ${user?.username}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_signOut -> {
                Timber.tag(TAG).d("User is signing out.")

                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnSuccessListener {
                        Timber.tag(TAG).d("User signed out successfully.")
                        findNavController()
                            .navigate(
                                HomeFragmentDirections.actionHomeFragmentToInitialFragment()
                            )
                    }
                    .addOnFailureListener {
                        Timber.tag(TAG).e("Error signing out user.")
                    }
                true
            }
            R.id.menu_item2 -> {
                Timber.tag(TAG).d("User clicked item 2")
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
