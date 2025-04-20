package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Product> productList;
    Button btnSearch, btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        // Change LinearLayoutManager to GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        btnSearch = findViewById(R.id.btn_search);
        btnCart = findViewById(R.id.btn_cart);

        ProductHelper.initializeProductsFromJson(this);
        loadProducts();

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        productList = ProductHelper.getAllProducts(this);
        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }
}