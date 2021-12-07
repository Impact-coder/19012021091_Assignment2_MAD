package com.example.a19012021091_assignment2_mad

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

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
        val timeFormat = view.findViewById<TextView>(R.id.tv_date)
        val layout_background = view.findViewById<LinearLayout>(R.id.main_liner)


        if (recordsList[position].paid == "paid")
        {

            layout_background.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.btn_gradient) );
//            color_change.background.setTint(
//                color_change.context.resources.getColor(
//                    R.color.black, null
//                )
//            )
        }
        else
        {
            layout_background.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.unpaid_gradient) );
//
//            layout.background.setTint(
//                layout.context.resources.getColor(
//                    R.color.white, null
//                )
//            )
        }


        customerName.text = recordsList[position].customerName
        customerNumber.text = recordsList[position].customerPhone
        billTotal.text = recordsList[position].totalPrice.toString()
        timeFormat.text = recordsList[position].date


        return view
    }
}