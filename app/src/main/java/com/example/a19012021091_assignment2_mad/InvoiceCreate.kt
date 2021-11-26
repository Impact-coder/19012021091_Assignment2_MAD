package com.example.a19012021091_assignment2_mad

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.sign

class InvoiceCreate : AppCompatActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_create)

        var matrialic_toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

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
        for (i in 0 until rates.size){
            amount += rates[i] * quantities[i]
            val t1 = rates[i] / 100 * taxes[i]
            tax += rates[i] + t1
        }

        amountBeforeTax.text = amount.toString()
        taxAmount.text = tax.toString()
        total.text = "${amount + tax}"

    }
}