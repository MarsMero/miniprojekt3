package com.example.miniprojekt3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.miniprojekt3.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val fbAuth = FirebaseAuth.getInstance()

    binding.btRegister.setOnClickListener {
      val email = binding.etLogin.text.toString()
      val password = binding.etPass.text.toString()
      fbAuth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
          Toast.makeText(this, "Succesfully registered", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
          Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
          Log.e(LoginActivity::class.simpleName, "Error while registering", it)
        }
    }

    binding.btLogin.setOnClickListener {
      val email = binding.etLogin.text.toString()
      val password = binding.etPass.text.toString()
      fbAuth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener {
          Toast.makeText(this, "${it.user?.email} logged in", Toast.LENGTH_SHORT).show()
          startActivity(Intent(this, ProductListActivity::class.java))
        }.addOnFailureListener {
          Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT).show()
          Log.e(LoginActivity::class.simpleName, "Error while logging in", it)
        }
    }
  }
}