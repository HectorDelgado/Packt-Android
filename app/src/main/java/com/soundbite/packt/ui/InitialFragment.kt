package com.soundbite.packt.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.databinding.FragmentInitialBinding

/**
 * A simple [Fragment] subclass.
 * Use the [InitialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class InitialFragment : Fragment() {
    private val binding
        get() = _binding!!
    private val RC_SIGN_IN = 2001
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private var _binding: FragmentInitialBinding? = null
    private var currentUser: FirebaseUser? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Authorization
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        _binding = FragmentInitialBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // (requireActivity() as DrawerLocker).setDrawerEnabled(false)
    }

    override fun onStart() {
        super.onStart()

        // auth.signOut()
        currentUser = auth.currentUser

        if (currentUser != null) {
            findNavController()
                .navigate(
                    InitialFragmentDirections.actionInitialFragmentToHomeFragment()
                )
        } else {
            // Launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in/created account
                Log.d("logz", "Signed in. Successfully!")
                findNavController()
                    .navigate(InitialFragmentDirections.actionInitialFragmentToHomeFragment())
            } else {
                if (response == null) {
                    // User cancelled sign-in flow
                    Log.d("logz", "Signed in cancelled")
                } else {
                    val error = response.error
                    Log.e("logz", "Error in sign in occurred.")
                    Log.e("logz", "Msg >> ${error?.message}")
                }
            }
        }
    }
}
