package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private Context context;
    private List<Product> cartList;
    private Map<String, Integer> itemCounts = new HashMap<>();
    private List<Product> uniqueItems = new ArrayList<>();

    public CartAdapter(Context context, List<Product> cartList) {
        this.context = context;
        this.cartList = cartList;
        processCartItems();
    }

    private void processCartItems() {
        itemCounts.clear();
        uniqueItems.clear();

        for (Product product : cartList) {
            String key = product.getName();
            itemCounts.put(key, itemCounts.getOrDefault(key, 0) + 1);
        }

        Map<String, Product> seen = new HashMap<>();
        for (Product product : cartList) {
            String key = product.getName();
            if (!seen.containsKey(key)) {
                seen.put(key, product);
                uniqueItems.add(product);
            }
        }
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = uniqueItems.get(position);
        int quantity = itemCounts.get(product.getName());

        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice() + " × " + quantity);
        holder.productQuantity.setText("Quantity: " + quantity);

        int imgId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
        holder.productImage.setImageResource(imgId);
    }

    @Override
    public int getItemCount() {
        return uniqueItems.size();
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : cartList) {
            total += product.getPrice();
        }
        return total;
    }
}