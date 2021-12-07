package com.example.a19012021091_assignment2_mad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BillDeatils : AppCompatActivity() {

    private val bills = FirebaseFirestore.getInstance().collection("bills")
    private val users = FirebaseFirestore.getInstance().collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_deatils)

        val itemNames: ArrayList<String> = arrayListOf()
        val sellingPrices: ArrayList<Float> = arrayListOf()
        val quantities: ArrayList<Int> = arrayListOf()
        val taxes: ArrayList<Float> = arrayListOf()
        val customerName = intent.getStringExtra("name")!!
        val customerPhone = intent.getStringExtra("phone")!!
        var total = 0f

        val item_name = findViewById<TextInputEditText>(R.id.item_name)
        val item_price = findViewById<TextInputEditText>(R.id.selling_price)
        val item_quantity = findViewById<TextInputEditText>(R.id.quantity)


        val tax = resources.getStringArray(R.array.tax_percentage)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_taxitem, tax)
        val item_tax_per = findViewById<AutoCompleteTextView>(R.id.tax_field)

        item_tax_per.setAdapter(arrayAdapter)

        val additem_btn = findViewById<AppCompatButton>(R.id.additem_btn)
        val finish_btn = findViewById<AppCompatButton>(R.id.finish_btn)

        additem_btn.setOnClickListener {

            if (item_name.text!!.isBlank() || item_price.text!!.isBlank() ||
                item_quantity.text!!.isBlank() || item_tax_per.text!!.isBlank()
            ) {
                Toast.makeText(
                    this,
                    "Please enter all fields\nAll fields are required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                itemNames.add(item_name.text.toString())
                sellingPrices.add(item_price.text.toString().toFloat())
                quantities.add(item_quantity.text.toString().toInt())
                val taxFinal = item_tax_per.text.toString().substringBefore("%")
                taxes.add(taxFinal.toFloat())
                val t =
                    item_price.text.toString().toFloat() * item_quantity.text.toString().toFloat()

                val taxAmount = t / 100 * taxFinal.toFloat()

                total += t + taxAmount

                item_name.setText("")
                item_price.setText("")
                item_quantity.setText("")
            }

        }

        finish_btn.setOnClickListener {
            if (itemNames.size == 0) {
                Toast.makeText(
                    this,
                    "There should be atleast one entry",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val timeFormat = SimpleDateFormat("MMM, dd yyyy ", Locale.ENGLISH)
                val timeStamp = timeFormat.format(System.currentTimeMillis())

                val id = bills.document().id
                val bill = BillData(
                    uid = id,
                    owner = Firebase.auth.currentUser!!.uid,
                    itemNames = itemNames,
                    sellingPrices = sellingPrices,
                    quantities = quantities,
                    taxes = taxes,
                    totalPrice = total,
                    date = timeStamp,
                    customerName = customerName,
                    customerPhone = customerPhone
                )

                CoroutineScope(Dispatchers.Main).launch {
                    bills.document(id).set(bill).await()
                    users.document(Firebase.auth.currentUser!!.uid)
                        .update("records", FieldValue.arrayUnion(id))
                }


                val rates = FloatArray(sellingPrices.size)
                for ((i, f) in sellingPrices.withIndex()) {
                    rates[i] = f
                }

                val tax1 = FloatArray(taxes.size)
                for ((i, t) in taxes.withIndex()) {
                    tax1[i] = t
                }

                Intent(this, InvoiceCreate::class.java).apply {
                    putExtra("items", itemNames)
                    putExtra("quantities", quantities)
                    putExtra("rates", rates)
                    putExtra("taxes", tax1)
                    putExtra("cst_num",customerPhone)
                    putExtra("date_of_invoice",timeStamp)
                    putExtra("id_bill",id)
                    putExtra("cst_name",customerName)
                    startActivity(this)
                }
            }
        }


    }
}