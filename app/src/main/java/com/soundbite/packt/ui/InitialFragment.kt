package com.soundbite.packt.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.R
import com.soundbite.packt.databinding.FragmentInitialBinding
import kotlinx.coroutines.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.UserProfileChangeRequest

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

//        binding.initialFragmentBtn.setOnClickListener {
//            findNavController().navigate(InitialFragmentDirections.actionInitialFragmentToHomeFragment())
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        if (currentUser != null) {
            // Redirect users to HomeFragment

        } else {
            // Launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)
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
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(requireContext(), "Signed in. Successfully!", Toast.LENGTH_LONG).show()
            } else {
                if (response == null) {
                    // User cancelled sign-in flow
                    Toast.makeText(requireContext(), "Sign in cancelled", Toast.LENGTH_LONG).show()
                } else {
                    val error = response.error
                    Log.e("logz", "Error in sign in occurred.")
                    Log.e("logz", "Msg >> ${error?.message}")
                }
            }
        }
    }
}