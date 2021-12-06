package com.example.a19012021091_assignment2_mad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_DIAL
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
import android.view.Menu
import android.view.MenuItem


class InvoiceCreate : AppCompatActivity() {

    private lateinit var btnSendMsg: AppCompatButton
    private val users = FirebaseFirestore.getInstance().collection("users")
    private val bills = FirebaseFirestore.getInstance().collection("bills")

    var id_bill:String =""

    var msgReminder: String = ""
    var msgBill: String = ""

    var customerPhone = ""
    var date:String =""
    var msg_array = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_create)
        val t = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(t)


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
         date = intent.getStringExtra("date_of_invoice")!!
        id_bill = intent.getStringExtra("id_bill")!!



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


            data += "\n\nItem ${i+1}" +
                    "\nName: ${items[i]}" +
                    "\nQty: ${quantities[i]}" +
                    "\nRate: ${rates[i]}" +
                    "\nGST%: ${taxes[i]} %"+
                    "\nTax Amount: ₹${t1}" +
                    "\nTotal: ₹${r + t1}"
        }



        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)!!
            val companyName = user.businessName
            val companyNumber = user.contactNumber
            msgBill = "$companyName\n$companyNumber" +
                    "$data" +
                    "\n\nTotal Amount: ${amount + tax}"




            msgReminder = "$companyName\n$companyNumber" + "\nYou have pending Bill of ₹${amount + tax} of the purchases you made on " +
                    "$date\nSo kindly pay the bill within two days."



            btn_send_sms.setOnClickListener {
                Toast.makeText(applicationContext, customerPhone, Toast.LENGTH_SHORT).show()
                permissionCheck(msgBill)

            }
        }


    }

    private fun permissionCheck(msg_permission:String) {

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
                sendMessage(msg_permission)
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

    private fun sendMessage(msg:String) {

        if (customerPhone == "" || msg == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (PhoneNumberUtils.isGlobalPhoneNumber(customerPhone)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                msg_array = smsManager.divideMessage(msg)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                smsManager.sendMultipartTextMessage(customerPhone.trim(), null, msg_array, null, null)
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
        grantResults: IntArray,

    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendMessage(msg_req)
            } else {
                Toast.makeText(
                    this, "You don't have required permission to send a message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendMessage(msg_req)
            } else {
                Toast.makeText(
                    this, "You don't have required permission to send a message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mark_as_paid_menu -> {
                CoroutineScope(Dispatchers.Main).launch {
                    bills.document(id_bill).update("ispaid",true).await()

                }

                Intent(this,RecordsDatabase::class.java).apply {
                    startActivity(this)
                }

                return true


            }
            R.id.additem_menu -> {
                Toast.makeText(applicationContext, "click on additem", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.call_menu -> {
                Intent(ACTION_DIAL).setData(Uri.parse("tel:" + customerPhone))
                    .apply {
                        startActivity(this)
                    }
                return true
            }
            else -> {
                permissionCheck(msgReminder)
                return true
            }
        }
    }


}

const val PERMISSION_REQUEST_SEND_SMS = 101
const val PERMISSION_REQUEST_READ_PHONE_STATE = 102
