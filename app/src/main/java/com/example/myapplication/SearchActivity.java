// SearchActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText searchInput;
    Button searchBtn;
    RecyclerView recyclerView;

    List<Product> allProducts;
    List<Product> searchResults = new ArrayList<>();
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button btnHome = findViewById(R.id.btn_home);
        Button btnCart = findViewById(R.id.btn_cart);
        searchInput = findViewById(R.id.search_input);
        searchBtn = findViewById(R.id.btn_search_now);
        recyclerView = findViewById(R.id.search_results_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, searchResults);
        recyclerView.setAdapter(adapter);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, Cart.class);
            startActivity(intent);
        });

        // Load products from SharedPreferences
        loadAllProducts();

        searchBtn.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim().toLowerCase();
            performSearch(query);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload products when returning to this activity
        loadAllProducts();
        // Re-run the search with current query
        if (searchInput.getText().length() > 0) {
            performSearch(searchInput.getText().toString().trim().toLowerCase());
        }
    }

    private void loadAllProducts() {
        allProducts = ProductHelper.getAllProducts(this);
    }

    private void performSearch(String query) {
        searchResults.clear();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query)) {
                searchResults.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }
}