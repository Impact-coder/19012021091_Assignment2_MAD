package com.example.a19012021091_assignment2_mad

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi

class RecordsAdapter(var context: Context, var recordsList: ArrayList<BillData>) : BaseAdapter() {

    override fun getCount(): Int {
        return recordsList.size
    }

    override fun getItem(position: Int): Any {
        return recordsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.list_item_records, parent, false)

        val customerName = view.findViewById<TextView>(R.id.tv_name)
        val customerNumber = view.findViewById<TextView>(R.id.tv_number)
        val billTotal = view.findViewById<TextView>(R.id.tv_bill_total)

        customerName.text = recordsList[position].customerName
        customerNumber.text = recordsList[position].customerPhone
        billTotal.text = recordsList[position].totalPrice.toString()



        return view
    }
}