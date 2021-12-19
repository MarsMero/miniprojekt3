package com.example.miniprojekt3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.miniprojekt3.data.Product
import com.example.miniprojekt3.databinding.ActivityEditProductBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProductActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityEditProductBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val key = intent.getStringExtra("key")

    title =
      if (key != null) "Edit product"
      else "Add product"

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("/products")

    if (key != null) ref.child(key).get().addOnSuccessListener {
      CoroutineScope(Dispatchers.IO).launch {
        val product = Product.fromDataSnapshot(it)
        withContext(Main) {
          binding.inName.setText(product.name)
          binding.inPrice.setText(product.price.toString())
          binding.inAmount.setText(product.amount.toString())
          binding.checkBox.isChecked = product.bought
        }
      }
    }

    binding.btCancel.setOnClickListener {
      startActivity(Intent(this, ProductListActivity::class.java))
    }

    binding.btSave.setOnClickListener {
      val product = Product(
        name = binding.inName.text.toString(),
        price = binding.inPrice.text.toString().toLong(),
        amount = binding.inAmount.text.toString().toLong(),
        bought = binding.checkBox.isChecked
      )


      if (key != null) ref.child(key).setValue(product)
      else ref.push().setValue(product)
    }

  }
}