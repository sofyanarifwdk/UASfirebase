package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.R.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private  lateinit var auth: FirebaseAuth
    private  lateinit var itemEmail: TextInputLayout
    private  lateinit var itemPassword: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_register)
        val linkToLogin : TextView = findViewById(R.id.link_to_login)
        auth = Firebase.auth
        supportActionBar?.hide()


        itemEmail = findViewById(R.id.itemEmail)  // Pastikan ID ini benar
        itemPassword = findViewById(R.id.itemPassword)  // Pastikan ID ini benar

        linkToLogin.setOnClickListener{
            val intentToLogin = Intent(this,LoginActivity::class.java)
            startActivity(intentToLogin)
        }

        val registerButton: TextView = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val email = itemEmail.editText?.text.toString()
            val password = itemPassword.editText?.text.toString()

            if (validateForm(email, password)) {
                registerUser(email, password)
            }
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.layoutRegister)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


    }


    private fun validateForm(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            itemEmail.error = "Alamat email tidak valid"
            itemEmail.requestFocus()
            return false
        }
        if (password.length < 6) {
            itemPassword.error = "Password harus memiliki minimal 6 karakter"
            itemPassword.requestFocus()
            return false
        }
        return true
    }

    private fun registerUser(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Registration success
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                // Redirect to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // Close current activity
            } else {
                // Registration failed
                Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}