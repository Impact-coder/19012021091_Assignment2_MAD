package com.example.a19012021091_assignment2_mad

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

            }
        }


    }
}