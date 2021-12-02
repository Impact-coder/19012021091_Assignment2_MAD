package com.example.a19012021091_assignment2_mad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
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
                    startActivity(this)
                }
            }
        }


    }
}