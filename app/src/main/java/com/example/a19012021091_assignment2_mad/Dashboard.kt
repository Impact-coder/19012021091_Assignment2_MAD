package com.example.a19012021091_assignment2_mad

import android.app.Dialog
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Dashboard : AppCompatActivity() {

    lateinit var pref: SharedPreferences

    private val users = FirebaseFirestore.getInstance().collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        setStatusBarTransparent()
        supportActionBar?.hide()

        val business_title = findViewById<TextView>(R.id.business_title)
        val business_add = findViewById<TextView>(R.id.business_add)

        val view_records = findViewById<AppCompatButton>(R.id.viewrecord)
        val create_invoice = findViewById<AppCompatButton>(R.id.create_invoice)

        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)!!

            business_title.text = user.businessName
            business_add.text = user.businessAddress

        }


//        pref = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

//        var title = pref.getString("business_name", null)
//        val tempadd = pref.getString("address", null)


        view_records.setOnClickListener {
            Intent(this, RecordsDatabase::class.java).apply {
                startActivity(this)
            }
        }

        create_invoice.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.customer_details_dialog)

            val customer_name = dialog.findViewById<TextInputEditText>(R.id.customer_name)
            val customer_phone = dialog.findViewById<TextInputEditText>(R.id.customer_number)

            val next_btn = dialog.findViewById<AppCompatButton>(R.id.next_btn)

            next_btn.setOnClickListener {
                if (customer_name.text.toString().isEmpty() or customer_phone.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(
                        this,
                        "Please enter all fields\nAll fields are required",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Intent(this, BillDeatils::class.java).apply {
                        putExtra("name", customer_name.text.toString())
                        putExtra("phone", customer_phone.text.toString())
                        startActivity(this)
                    }


                }
            }
            dialog.show()


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