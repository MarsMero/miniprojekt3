package com.example.miniprojekt3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojekt3.data.ProductAdapter
import com.example.miniprojekt3.databinding.ActivityProductListBinding
import com.google.firebase.database.FirebaseDatabase


class ProductListActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityProductListBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("/products")

    binding.recyclerview.layoutManager = LinearLayoutManager(this)


    val products = arrayListOf<ProductAdapter.Companion.Element>()
    val adapter = ProductAdapter(this, products, ref)
    binding.recyclerview.adapter = adapter


    binding.fab.setOnClickListener {
      val intent = Intent(this, EditProductActivity::class.java)
      startActivity(intent)
    }
  }
}