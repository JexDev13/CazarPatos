package com.jeremy.arias.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class LoginActivity : AppCompatActivity() {
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword:EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser:Button
    lateinit var mediaPlayer: MediaPlayer
    lateinit var manejadorArchivo: FileHandler
    lateinit var checkBoxRecordarme: CheckBox

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        //Inicialización de variables
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        //manejadorArchivo = SharedPreferencesManager(this)
        manejadorArchivo = EncriptedSharedPreferences(this)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)

        auth = Firebase.auth

        LeerDatosDePreferencias()

        //Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()
            //Validaciones de datos requeridos y formatos
            if(!validateRequiredData())
                return@setOnClickListener

            GuardarDatosEnPreferencias()

            //Si pasa validación de datos requeridos, ir a pantalla principal
            //val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra(EXTRA_LOGIN, email)
            //startActivity(intent)
            //finish()
            AutenticarUsuario(email, clave)
        }
        buttonNewUser.setOnClickListener{
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()
            //Validaciones de datos requeridos y formatos
            if(!validateRequiredData())
                return@setOnClickListener

            SignUpNewUser(email, clave)

        }
        mediaPlayer=MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }
    private fun validateRequiredData():Boolean{
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
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
        if (password.length < 3) {
            editTextPassword.setError(getString(R.string.error_password_min_length))
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    private fun LeerDatosDePreferencias(){
        val listadoLeido = manejadorArchivo.ReadInformation()
        if(listadoLeido.first != null){
            checkBoxRecordarme.isChecked = true
        }
        editTextEmail.setText ( listadoLeido.first )
        editTextPassword.setText ( listadoLeido.second )
    }

    private fun GuardarDatosEnPreferencias(){
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val listadoAGrabar:Pair<String,String>
        if(checkBoxRecordarme.isChecked){
            listadoAGrabar = email to clave
        }
        else{
            listadoAGrabar ="" to ""
        }
        manejadorArchivo.SaveInformation(listadoAGrabar)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, currentUser.email)
            startActivity(intent)
        }
            //reload();
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun AutenticarUsuario(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "signInWithEmail:success")
                    //Si pasa validación de datos requeridos, ir a pantalla principal
                    val intencion = Intent(this, MainActivity::class.java)
                    intencion.putExtra(EXTRA_LOGIN, auth.currentUser!!.email)
                    startActivity(intencion)
                    //finish()
                } else {
                    Log.w(EXTRA_LOGIN, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception!!.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun SignUpNewUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(EXTRA_LOGIN, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "New user saved.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(EXTRA_LOGIN, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

}