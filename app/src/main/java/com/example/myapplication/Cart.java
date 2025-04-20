// Cart.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    List<Product> cartItems;
    TextView totalPriceText;
    Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceText = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);

        loadCartItems();

        findViewById(R.id.btn_home).setOnClickListener(v -> {
            startActivity(new Intent(Cart.this, MainActivity.class));
            finish();
        });

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(Cart.this, SearchActivity.class));
        });

        findViewById(R.id.btn_cart).setOnClickListener(v -> {
            Toast.makeText(this, "You are already in the Cart", Toast.LENGTH_SHORT).show();
        });

        // Checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Confirm checkout dialog
            new AlertDialog.Builder(this)
                    .setTitle("Checkout")
                    .setMessage("Confirm your purchase of " + cartItems.size() + " items for $" + String.format("%.2f", adapter.getTotalPrice()))
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        // Process checkout
                        processCheckout();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadCartItems() {
        cartItems = CartHelper.getCartItems(this);
        adapter = new CartAdapter(this, cartItems);
        recyclerView.setAdapter(adapter);

        // Update total price
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        if (adapter != null && totalPriceText != null) {
            double total = adapter.getTotalPrice();
            totalPriceText.setText("Total: $" + String.format("%.2f", total));
        }
    }

    private void processCheckout() {
        if (cartItems.isEmpty()) {
            return;
        }

        // Get counts of each product in the cart
        Map<String, Integer> itemCounts = CartHelper.getCartItemCounts(this);

        // Decrement each product's quantity by the number purchased
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            ProductHelper.decrementProductQuantity(this, productName, quantity);
        }

        // Clear the cart after checkout is complete
        CartHelper.clearCart(this);

        // Show success message
        Toast.makeText(this, "Checkout successful! Thank you for your purchase.", Toast.LENGTH_LONG).show();

        // Return to main activity
        Intent intent = new Intent(Cart.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}