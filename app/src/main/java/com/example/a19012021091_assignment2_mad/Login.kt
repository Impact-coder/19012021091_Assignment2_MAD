package com.example.a19012021091_assignment2_mad

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var pref: SharedPreferences

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        setStatusBarTransparent()
        supportActionBar?.hide()



        val text_signup = findViewById<TextView>(R.id.text_signup)

        text_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply {
                startActivity(this)
            }
        }

//        if (LoginInfo.state == true)
//        {
//            Intent(this, Dashboard_MainActivity::class.java).apply {
//                startActivity(this)
//            }
//
//        }
//        else
//        {
            val login_btn = findViewById<AppCompatButton>(R.id.login_btn)
            val login_email = findViewById<TextInputEditText>(R.id.email_id)
            val login_password = findViewById<TextInputEditText>(R.id.password)

            login_btn.setOnClickListener {

                if (login_email.length() == 0 || login_password.length() == 0) {
                    Toast.makeText(this, "Any field can't be empty!!", Toast.LENGTH_SHORT).show()
                } else {

                    auth.signInWithEmailAndPassword(
                        login_email.text.toString(),
                        login_password.text.toString()
                    )

//                    pref = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
//                    val shared_email = pref.getString("email",null)
//                    val shared_password = pref.getString("password",null)
//
//
//                    if ((login_email.text.toString() == shared_email) && (login_password.text.toString() == shared_password))
//                    {
                    Intent(this, Dashboard::class.java).apply {
                        startActivity(this)
//                        }
////                        LoginInfo.state = true
//                    }
//                    else
//                    {
//                        login_email.setText("")
//                        login_password.setText("")
//                        Toast.makeText( this,"Invalid Email-Id or Password!!", Toast.LENGTH_SHORT).show()
//
//                    }
                    }


                }

//        }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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