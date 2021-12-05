package com.example.a19012021091_assignment2_mad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.SmsManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.sign

class InvoiceCreate : AppCompatActivity() {

    private  var etPhoneNo=""
    private  var etMessage =""
    private lateinit var btnSendMsg: AppCompatButton


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_create)



        var btn_back_to_store = findViewById<AppCompatButton>(R.id.btn_back_to_home)

//        var matrialic_toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)


        btn_back_to_store.setOnClickListener {
            Intent(this,RecordsDatabase::class.java).apply {
                startActivity(this)
            }
        }

        val items = intent.getStringArrayListExtra("items")!!
        val quantities = intent.getIntegerArrayListExtra("quantities")!!
        val rates = intent.getFloatArrayExtra("rates")!!
        val taxes = intent.getFloatArrayExtra("taxes")!!

        val adapter =
            InvoiceAdapter(
                application,
                items = items,
                quantities = quantities,
                rates = rates,
                taxes = taxes
            )
        val lvInvoice = findViewById<ListView>(R.id.bill_listview)
        lvInvoice.adapter = adapter

        val amountBeforeTax = findViewById<TextView>(R.id.amount_before_tax)
        val taxAmount = findViewById<TextView>(R.id.tax_amount)
        val total = findViewById<TextView>(R.id.total_amount)

        var amount = 0f
        var tax = 0f
        for (i in 0 until rates.size) {
            amount += rates[i] * quantities[i]
            val t1 = rates[i] / 100 * taxes[i]
            tax += rates[i] + t1
        }

        amountBeforeTax.text = amount.toString()
        taxAmount.text = tax.toString()
        total.text = "${amount + tax}"

    }

//    private fun permissionCheck() {
//        val permissionCheck = ContextCompat.checkSelfPermission(this,
//            Manifest.permission.SEND_SMS)
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            val permissionCheck1 =
//                ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_PHONE_STATE)
//            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
//                sendMessage()
//            } else {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.READ_PHONE_STATE),
//                    PERMISSION_REQUEST_SEND_SMS
//                )
//            }
//        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.SEND_SMS),
//                PERMISSION_REQUEST_SEND_SMS
//            )
//        }
//    }
//    private fun sendMessage() {
//        val number: String = etPhoneNo.text.toString().trim()
//        val msg: String = etMessage.text.toString().trim()
//        if (number == "" || msg == "") {
//            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
//        } else {
//            if (PhoneNumberUtils.isGlobalPhoneNumber(number)) {
//                val smsManager: SmsManager = SmsManager.getDefault()
//                smsManager.sendTextMessage(number, null, msg, null, null)
//                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
//                etMessage.setText("")
//
//            } else {
//                Toast.makeText(this, "Please enter the correct number",
//                    Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendMessage()
//            } else {
//                Toast.makeText(
//                    this, "You don't have required permission to send a message",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        } else if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendMessage()
//            } else {
//                Toast.makeText(
//                    this, "You don't have required permission to send a message",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.top_app_bar, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.additem_menu -> {
//                Intent(this, BillDeatils::class.java).apply {
//                    startActivity(this)
//                }
//                true
//            }
//            R.id.save_as_pdf_menu -> {
//                Toast.makeText(applicationContext, "Saved as pdf", Toast.LENGTH_LONG).show()
//                return true
//            }
//            R.id.send_to_cuatomer_menu -> {
//                Toast.makeText(applicationContext, "Send to Customer", Toast.LENGTH_LONG).show()
//                return true
//            }
//            else -> {
//                Intent(this, Dashboard::class.java).apply {
//                    startActivity(this)
//                }
//                true
//            }
//        }
//    }
}

const val PERMISSION_REQUEST_SEND_SMS = 101
const val PERMISSION_REQUEST_READ_PHONE_STATE = 102
const val PERMISSION_REQUEST_READ_SMS = 103