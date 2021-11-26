package com.example.a19012021091_assignment2_mad

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Signup : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("users")


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        setStatusBarTransparent()
        supportActionBar?.hide()


        val text_login = findViewById<TextView>(R.id.text_login)



        text_login.setOnClickListener {
            Intent(this, Login::class.java).apply {
                startActivity(this)

            }
        }


        val signup_btn = findViewById<AppCompatButton>(R.id.signup_btn)


        signup_btn.setOnClickListener {

            val signup_name = findViewById<TextInputEditText>(R.id.business_name)
            val signup_phone_no = findViewById<TextInputEditText>(R.id.contact_number)
            val signup_add = findViewById<TextInputEditText>(R.id.business_address)
            val signup_email = findViewById<TextInputEditText>(R.id.email_id)
            val signup_password = findViewById<TextInputEditText>(R.id.password)
            val signup_confrim_password = findViewById<TextInputEditText>(R.id.confrim_password)




            if (signup_name.length() == 0 || signup_phone_no.length() == 0 || signup_add.length() == 0 || signup_email.length() == 0 || signup_password.length() == 0) {
                Toast.makeText(this, "Any field can't be empty!!", Toast.LENGTH_SHORT).show()
            } else if (signup_confrim_password.text.toString() != signup_password.text.toString()) {
                signup_name.setText("")
                signup_phone_no.setText("")
                signup_add.setText("")
                signup_email.setText("")
                signup_password.setText("")
                signup_confrim_password.setText("")
                Toast.makeText(
                    this,
                    "Password and Confirm Password Must be Match!!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                CoroutineScope(Dispatchers.Main).launch {

                    try {
                        auth.createUserWithEmailAndPassword(
                            signup_email.text.toString(),
                            signup_password.text.toString()
                        ).await()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }


                    if (auth.currentUser != null) {
                        val uid = auth.currentUser!!.uid
                        val user = User(
                            uid = uid,
                            businessName = signup_name.text.toString(),
                            email = signup_email.text.toString(),
                            businessAddress = signup_add.text.toString(),
                            contactNumber = signup_phone_no.text.toString()
                        )

                        users.document(uid).set(user).await()

                        Toast.makeText(
                            applicationContext,
                            "Successfully Registered!!",
                            Toast.LENGTH_SHORT
                        ).show()

                        Intent(applicationContext, Login::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    }


                }

            }


        }
    }


    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParameters = window.attributes
        if (on) {
            winParameters.flags = winParameters.flags or bits
        } else {
            winParameters.flags = winParameters.flags and bits.inv()
        }
        window.attributes = winParameters
    }

}