package com.example.a19012021091_assignment2_mad

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi

class InvoiceAdapter(
    var context: Context,
    var items: ArrayList<String>,
    var quantities: ArrayList<Int>,
    var rates: FloatArray,
    var taxes: FloatArray
) :
    BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.invoice_item_layout, parent, false)

        val srNo = view.findViewById<TextView>(R.id.tv_sr_no)
        val itemName = view.findViewById<TextView>(R.id.tv_item_name)
        val quantity = view.findViewById<TextView>(R.id.tv_quantity)
        val rate = view.findViewById<TextView>(R.id.tv_rate)
        val tax = view.findViewById<TextView>(R.id.tv_tax)
        val taxAmount = view.findViewById<TextView>(R.id.tv_tax_amount)
        val total = view.findViewById<TextView>(R.id.tv_total)

        srNo.text = "${position + 1}"
        itemName.text = items[position]
        quantity.text = quantities[position].toString()
        tax.text = "${taxes[position]}%"

        val r = rates[position] * quantities[position]

        rate.text = r.toString()

        val t1 = r / 100 * taxes[position]

        taxAmount.text = t1.toString()
        total.text = "${r + t1}"

        return view
    }
}