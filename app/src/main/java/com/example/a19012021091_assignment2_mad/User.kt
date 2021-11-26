package com.example.a19012021091_assignment2_mad

data class User(
    val uid: String = "",
    val businessName: String = "",
    val email: String = "",
    val businessAddress: String = "",
    val contactNumber: String = "",
    val records: ArrayList<String> = arrayListOf()
)
