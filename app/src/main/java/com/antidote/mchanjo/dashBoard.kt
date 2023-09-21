package com.antidote.mchanjo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antidote.mchanjo.viewmodels.MainActivityViewModel
import com.google.android.fhir.sync.SyncJobStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class dashBoard : Fragment() {
  private lateinit var auth: FirebaseAuth

  private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

    //        Initialize FirebaseAuth Instance
    auth = FirebaseAuth.getInstance()
    //        LOGOUT CARD VIEW
    //        Find the logout card view
    val logoutCardView: CardView = view.findViewById(R.id.logout_card)

    //        Set Click listener on logout card view
    logoutCardView.setOnClickListener {
      //            Sign out the user
      auth.signOut()
      //            Show a toast message
      Toast.makeText(
              requireContext(),
              "Goodbye \uD83D\uDE4B \uD83D\uDE4B\u200D♂️, you are logged out Clinician",
              Toast.LENGTH_LONG)
          .show()
      findNavController().navigate(R.id.action_dashBoard_to_loginAuthO)
    }
    //        E LEARNING VIEW
    val learningCardView: CardView = view.findViewById(R.id.learning)
    learningCardView.setOnClickListener {
      findNavController().navigate(R.id.action_dashBoard_to_vaccine_Guidelines)
    }
    //        REGISTER VIEW
    val registerCardView: CardView = view.findViewById(R.id.register)
    registerCardView.setOnClickListener {
      findNavController().navigate(R.id.action_dashBoard_to_registerInfant)
    }
    //          SYNC
    val syncCardView: CardView = view.findViewById(R.id.sync)
    syncCardView.setOnClickListener {
      mainActivityViewModel.triggerOneTimeSync()
    }

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launch {
      mainActivityViewModel.pollState.collect {
        when (it) {
          is SyncJobStatus.Started -> {
            Toast.makeText(
              requireContext(),
              "Syncing",
              Toast.LENGTH_LONG)
              .show()
          }
          is SyncJobStatus.InProgress -> {
            Toast.makeText(requireContext(), "Syncing...", Toast.LENGTH_SHORT).show()
          }
          is SyncJobStatus.Finished -> {
            Toast.makeText(requireContext(), "Finished", Toast.LENGTH_SHORT).show()
          }
          is SyncJobStatus.Failed -> {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
          }
          else -> {

            Toast.makeText(requireContext(), "Sync: Unknown state", Toast.LENGTH_SHORT).show()
          }
        }
      }
    }
  }
}
