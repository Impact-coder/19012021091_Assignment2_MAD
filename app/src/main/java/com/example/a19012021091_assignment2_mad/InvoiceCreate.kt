package com.example.a19012021091_assignment2_mad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.SmsManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.content.pm.PackageInfo
import android.net.Uri


class InvoiceCreate : AppCompatActivity() {

    private var etPhoneNo = ""
    private var etMessage = ""
    private lateinit var btnSendMsg: AppCompatButton
    private val users = FirebaseFirestore.getInstance().collection("users")

    var msg: String = ""
    var customerPhone = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_create)


        var btn_back_to_store = findViewById<AppCompatButton>(R.id.btn_back_to_home)
        var btn_send_sms = findViewById<AppCompatButton>(R.id.btn_send_sms)


        btn_back_to_store.setOnClickListener {
            Intent(this, RecordsDatabase::class.java).apply {
                startActivity(this)
            }
        }

        val items = intent.getStringArrayListExtra("items")!!
        val quantities = intent.getIntegerArrayListExtra("quantities")!!
        val rates = intent.getFloatArrayExtra("rates")!!
        val taxes = intent.getFloatArrayExtra("taxes")!!
        customerPhone = intent.getStringExtra("cst_num")!!


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
            val a = rates[i] * quantities[i]
            amount += a
            val t1 = a / 100 * taxes[i]
            tax += t1
        }

        amountBeforeTax.text = amount.toString()
        taxAmount.text = tax.toString()
        total.text = "${amount + tax}"

        var data = ""

        for (i in 0..items.size - 1) {

            val r = rates[i] * quantities[i]

            val t1 = r / 100 * taxes[i]

            data += "\n${items[i]} ${rates[i]} ${t1} ${r + t1}"
        }



        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)!!
            val companyName = user.businessName
            val companyNumber = user.contactNumber

            msg = "$companyName \n $companyNumber" +
                    "\nName Rate TAX Total" +
                    "$data" +
                    "\nTotal Amount: ${amount + tax}"

            btn_send_sms.setOnClickListener {
                Toast.makeText(applicationContext, customerPhone, Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }


    }

    private fun permissionCheck() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val permissionCheck1 =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                )
            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
                sendMessage()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_PHONE_STATE),
                    PERMISSION_REQUEST_SEND_SMS
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_REQUEST_SEND_SMS
            )
        }
    }

    private fun sendMessage() {

        if (customerPhone == "" || msg == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (PhoneNumberUtils.isGlobalPhoneNumber(customerPhone)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                smsManager.sendTextMessage(customerPhone.trim(), null, msg, null, null)
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(
                    this, "Please enter the correct number",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage()
            } else {
                Toast.makeText(
                    this, "You don't have required permission to send a message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage()
            } else {
                Toast.makeText(
                    this, "You don't have required permission to send a message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}

const val PERMISSION_REQUEST_SEND_SMS = 101
const val PERMISSION_REQUEST_READ_PHONE_STATE = 102
