package com.muneeb.gym.data

data class Payment(
    val id: Long,
    val memberId: Int,
    val amount: Double,
    val paymentDate: String,
    val status: String
)

