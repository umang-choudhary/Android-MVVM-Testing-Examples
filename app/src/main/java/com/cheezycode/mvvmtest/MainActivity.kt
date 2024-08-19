package com.cheezycode.mvvmtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cheezycode.mvvmtest.adapter.ProductAdapter
import com.cheezycode.mvvmtest.utils.NetworkResult
import com.cheezycode.mvvmtest.viewmodels.MainViewModel
import com.cheezycode.mvvmtest.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.productList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val repository = (application as StoreApplication).productRepository
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getProducts()

        mainViewModel.products.observe(this, Observer {
            when(it){
                is NetworkResult.Success -> {
                    Log.d("CHEEZ", it.data.toString())
                    adapter = ProductAdapter(it.data!!)
                    recyclerView.adapter = adapter

                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> TODO()
            }
        })
    }
}