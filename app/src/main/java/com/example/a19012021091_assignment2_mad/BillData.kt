package com.example.a19012021091_assignment2_mad

data class BillData(
    val uid: String = "",
    val owner: String = "",
    val itemNames: ArrayList<String> = arrayListOf(),
    val sellingPrices: ArrayList<Float> = arrayListOf(),
    val quantities: ArrayList<Int> = arrayListOf(),
    val taxes: ArrayList<Float> = arrayListOf(),
    val totalPrice: Float = 0f,
    val date: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val paid:String = "notPaid"
)
