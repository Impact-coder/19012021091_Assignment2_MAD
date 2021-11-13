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

class BillDeatils : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)   {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_deatils)

        val item_name = findViewById<TextInputEditText>(R.id.item_name)
        val item_price = findViewById<TextInputEditText>(R.id.selling_price)
        val item_quantity= findViewById<TextInputEditText>(R.id.quantity)


        val tax = resources.getStringArray(R.array.tax_percentage)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_taxitem, tax)
        val item_tax_per = findViewById<AutoCompleteTextView>(R.id.tax_field)

        item_tax_per.setAdapter(arrayAdapter)

        val additem_btn = findViewById<AppCompatButton>(R.id.additem_btn)
        val finish_btn = findViewById<AppCompatButton>(R.id.finish_btn)

        additem_btn.setOnClickListener {

            item_name.setText("")
            item_price.setText("")
            item_quantity.setText("")

        }

        finish_btn.setOnClickListener {
            if (item_name.text.toString().isEmpty() or item_price.text.toString().isEmpty() or
                item_quantity.text.toString().isEmpty() or item_tax_per.text.toString().isEmpty())
            {
                Toast.makeText(
                    this,
                    "Please enter all fields\nAll fields are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
            {
                Intent(this, InvoiceCreate::class.java).apply {
                    startActivity(this)
                }
            }
        }



    }
}