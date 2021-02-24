package com.soundbite.packt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.IdpResponse
import com.soundbite.packt.databinding.FragmentInitialBinding
import com.soundbite.packt.domain.AuthResultContract
import com.soundbite.packt.network.RemoteDataBaseViewModelFactory
import com.soundbite.packt.network.RemoteDatabaseViewModel
import timber.log.Timber

/**
 * Initial landing fragment for all users. This fragment is used to authenticate the current user.
 * If no user is singed in we start an Intent for the AuthUI builder. After authentication has
 * passed we redirect the user to the home page.
 */

class InitialFragment : Fragment() {
    private val binding
        get() = _binding!!

    private val remoteDatabaseViewModel: RemoteDatabaseViewModel by viewModels {
        RemoteDataBaseViewModelFactory()
    }

    // Registers AuthResultContract to start an AuthUI Activity for a result.
    private val authResultLauncher =
        registerForActivityResult(AuthResultContract()) { idpResponse ->
            handleAuthResponse(idpResponse)
        }

    private val TAG = "T-${javaClass.simpleName}"

    private var _binding: FragmentInitialBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        _binding = FragmentInitialBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Handles the result of AuthResultContract
     */
    private fun handleAuthResponse(idpResponse: IdpResponse?) {
        when {
            (idpResponse == null) -> {
                // Handle Error
                Timber.tag(TAG).d("User cancelled sign in flow.")
                Toast.makeText(
                    requireContext(),
                    "Sign in cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            }
            idpResponse.error != null -> {
                Timber.tag(TAG).e("Error found in idpResponse. Error ${idpResponse.error}")
                Toast.makeText(
                    requireContext(),
                    "Error occurred signing in.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                // Handle sign in process
                Timber.tag(TAG).d("Signed in successfully.")
                Toast.makeText(
                    requireContext(),
                    "Signed in successfully",
                    Toast.LENGTH_LONG
                ).show()
                // remoteDatabaseViewModel.updateUserStatus()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Timber.tag(TAG).d("onResume called")

        remoteDatabaseViewModel.currentUser.let {
            if (it != null) {
                Timber.tag(TAG).d("User is already signed in. Going home")
                findNavController()
                    .navigate(
                        InitialFragmentDirections.actionInitialFragmentToHomeFragment()
                    )
            } else {
                Timber.tag(TAG).d("User is not signed in. Starting AuthUI")
                authResultLauncher.launch(AuthResultContract.REQUEST_CODE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
