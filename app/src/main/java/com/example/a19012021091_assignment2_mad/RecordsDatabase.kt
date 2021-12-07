package com.example.a19012021091_assignment2_mad

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RecordsDatabase : AppCompatActivity() {

    private val bills = FirebaseFirestore.getInstance().collection("bills")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.records_database)

        var btn_create_invoice = findViewById<FloatingActionButton>(R.id.btn_create_invoice)


        CoroutineScope(Dispatchers.Main).launch {

            val listItems =
                bills.whereEqualTo("owner", Firebase.auth.currentUser!!.uid).get().await()
                    .toObjects(BillData::class.java) as ArrayList<BillData>

            val adapter = RecordsAdapter(application, listItems)
            val lvRecords = findViewById<ListView>(R.id.record_list)
            lvRecords.adapter = adapter

            lvRecords.setOnItemClickListener { adapterView, view, i, l ->
                val item = adapter.getItem(position = i) as BillData

                val rates = FloatArray(item.sellingPrices.size)
                for ((j, f) in item.sellingPrices.withIndex()) {
                    rates[j] = f
                }

                val tax1 = FloatArray(item.taxes.size)
                for ((j, t) in item.taxes.withIndex()) {
                    tax1[j] = t
                }

                Intent(applicationContext, InvoiceCreate::class.java).apply {
                    putExtra("items", item.itemNames)
                    putExtra("quantities", item.quantities)
                    putExtra("rates", rates)
                    putExtra("taxes", tax1)
                    putExtra("cst_num",item.customerPhone)
                    putExtra("date_of_invoice",item.date)
                    putExtra("id_bill",item.uid)
                    startActivity(this)
                }
            }
        }

        btn_create_invoice.setOnClickListener {

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

                    if ((customer_phone.text.toString()).length != 10) {
                        Toast.makeText(
                            this,
                            "Please enter valid Phone number",
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
            }
            dialog.show()

        }


    }
}