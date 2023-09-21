package com.antidote.mchanjo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class LoginAuthO : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_auth_o, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Declaring the fields
        val emailEditText = view.findViewById<EditText>(R.id.login_email)
        val passwordEditText = view.findViewById<EditText>(R.id.login_password)
        val loginButton = view.findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Navigate to the dashboard
                    findNavController().navigate(R.id.action_loginAuthO_to_dashBoard)
//                    Toast
                    Toast.makeText(context, "Successfully authorized\uD83D\uDC4F\uD83D\uDD25\uD83D\uDD25 ", Toast.LENGTH_SHORT).show()

                } else {
                    // If sign-in fails, display a message to the user
                    Toast.makeText(context, "\uD83D\uDCA5\uD83D\uDCA5Bad credentials contact developer\uD83D\uDE21 \uD83E\uDD2C \uD83E\uDD2F", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
