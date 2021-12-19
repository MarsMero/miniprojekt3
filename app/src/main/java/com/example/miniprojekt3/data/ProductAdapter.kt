package com.example.miniprojekt3.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprojekt3.EditProductActivity
import com.example.miniprojekt3.R
import com.example.miniprojekt3.databinding.RecyclerviewItemBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductAdapter(val context: Context, val products: ArrayList<Element>, val ref: DatabaseReference) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

  inner class ProductViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

  }

  companion object {
    data class Element(val key: String, val product: Product)
  }

  init {
    ref.addChildEventListener(object: ChildEventListener {
      override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        CoroutineScope(IO).launch {
          val product = Product.fromDataSnapshot(snapshot)
          products.add(Element(snapshot.key!!, product))
          withContext(Main) {
            notifyDataSetChanged()
          }
        }
      }

      override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        CoroutineScope(IO).launch {
          val product = Product.fromDataSnapshot(snapshot)

          val index = products.indexOfFirst { it.key == snapshot.key }
          products[index] = Element(snapshot.key!!, product)

          withContext(Main) {
            notifyDataSetChanged()
          }
        }
      }

      override fun onChildRemoved(snapshot: DataSnapshot) {
        CoroutineScope(IO).launch {
          val index = products.indexOfFirst { it.key == snapshot.key }
          products.removeAt(index)
          withContext(Main) {
            notifyDataSetChanged()
          }
        }
      }

      override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

      }

      override fun onCancelled(error: DatabaseError) {

      }

    })
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

    val inflater = LayoutInflater.from(parent.context)
    val binding = RecyclerviewItemBinding.inflate(inflater)
    return ProductViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
    val element = products[position]
    val currentProduct = element.product
    holder.binding.tName.text = currentProduct.name
    holder.binding.tAmount.text = currentProduct.amount.toString()
    holder.binding.tValue.text = currentProduct.price.toString()
    if (currentProduct.bought) holder.binding.cxBought.setCheckMarkDrawable(R.drawable.ic_baseline_check_24)
    else holder.binding.cxBought.setCheckMarkDrawable(R.drawable.ic_baseline_close_24)


    holder.binding.btDelete.setOnClickListener {
      ref.child(element.key).removeValue()
    }

    holder.binding.btEdit.setOnClickListener {
      val intent = Intent(context, EditProductActivity::class.java)
      intent.putExtra("key", element.key)
      context.startActivity(intent)
    }
  }

  override fun getItemCount(): Int = products.size


}