package com.jeremy.arias.cazarpatos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextRepeatPassword: EditText
    lateinit var buttonRegister: Button
    lateinit var buttonBackLogin: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextRepeatPassword = findViewById(R.id.confirmTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonBackLogin = findViewById(R.id.buttonBackLogin)

        auth = Firebase.auth

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()

            if(!validateRequiredData())
                return@setOnClickListener

            SignUpNewUser(email, clave)
        }

        buttonBackLogin.setOnClickListener {
            val intencion = Intent(this, LoginActivity::class.java)
            startActivity(intencion)
        }
    }

    private fun validateRequiredData():Boolean{
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val repetirClave = editTextRepeatPassword.text.toString()

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email_required))
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_password_required))
            editTextPassword.requestFocus()
            return false
        }

        if (repetirClave.isEmpty())
        {
            editTextRepeatPassword.setError(getString(R.string.error_password_required))
            editTextRepeatPassword.requestFocus()
            return false
        }
        if (password.length < 6 || repetirClave.length < 6) {
            editTextPassword.setError(getString(R.string.error_password_min_length))
            editTextRepeatPassword.setError(getString(R.string.error_password_min_length))
            editTextPassword.requestFocus()
            return false
        }

        if (!password.equals(repetirClave))
        {
            editTextRepeatPassword.setError(getString(R.string.error_password_repeat))
            editTextRepeatPassword.requestFocus()
            return false
        }
        return true
    }

    private fun SignUpNewUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "New user saved.",  Toast.LENGTH_SHORT).show()
                    val intencion = Intent(this, LoginActivity::class.java)
                    startActivity(intencion)
                } else {
                    Log.w(EXTRA_LOGIN, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}