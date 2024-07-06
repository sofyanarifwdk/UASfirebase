package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var itemEmail: TextInputLayout
    private lateinit var itemPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth

        if(getSupportActionBar() != null){
            getSupportActionBar()?.hide();
        }

        setContentView(R.layout.activity_login)
        val linkToRegister : TextView = findViewById(R.id.link_to_register)
        linkToRegister.setOnClickListener{
            val intentToRegister = Intent(this,RegisterActivity::class.java)
            startActivity(intentToRegister)
        }

        itemEmail = findViewById(R.id.itemEmail)
        itemPassword = findViewById(R.id.itemPassword)

        val buttonLogin : Button = findViewById(R.id.loginButton)
        buttonLogin.setOnClickListener{
            val email = itemEmail.editText?.text.toString()
            val password = itemPassword.editText?.text.toString()

            if(validateEmailPassword(email, password )){
                loginUser(email,password)
            }
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    private fun validateEmailPassword(email: String , password: String) : Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            itemEmail.error = "Alamat Email Tidak Valid!"
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

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
           task -> if(task.isSuccessful){
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, redirect to another activity or update UI
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}