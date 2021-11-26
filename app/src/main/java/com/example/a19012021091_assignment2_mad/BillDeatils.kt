package com.example.a19012021091_assignment2_mad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BillDeatils : AppCompatActivity() {

    private val bills = FirebaseFirestore.getInstance().collection("bills")

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

                val id = bills.document().id
                val bill = BillData(
                    uid = id,
                    itemNames = itemNames,
                    sellingPrices = sellingPrices,
                    quantities = quantities,
                    taxes = taxes,
                    totalPrice = total,
                    customerName = customerName,
                    customerPhone = customerPhone
                )

                CoroutineScope(Dispatchers.Main).launch {
                    bills.document(id).set(bill).await()
                }


                Intent(this, InvoiceCreate::class.java).apply {
                    startActivity(this)
                }
            }
        }


    }
}