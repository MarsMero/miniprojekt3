package com.example.miniprojekt3.data

import com.google.firebase.database.DataSnapshot

data class Product(
  val name: String = "",
  val price: Long = 0,
  val amount: Long = 0,
  val bought: Boolean = false
) {
  companion object {
    suspend fun fromDataSnapshot(snapshot: DataSnapshot): Product {
      return Product(
        name = snapshot.child("name").value as String,
        price = snapshot.child("price").value as Long,
        amount = snapshot.child("amount").value as Long,
        bought = snapshot.child("bought").value as Boolean
      )
    }
  }
}