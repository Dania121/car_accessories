package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.productList = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.productName.setText(p.getName());
        holder.productPrice.setText("$" + p.getPrice());

        if (holder.productQuantity != null) {
            holder.productQuantity.setText("Available: " + p.getQuantity());
        }

        int imgId = context.getResources().getIdentifier(p.getImage(), "drawable", context.getPackageName());
        holder.productImage.setImageResource(imgId);

        holder.quantityCounter.setText("1");

        // Increment button logic
        holder.btnIncrement.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(holder.quantityCounter.getText().toString());
            if (currentQty < p.getQuantity()) {
                holder.quantityCounter.setText(String.valueOf(currentQty + 1));
            } else {
                Toast.makeText(context, "Maximum available quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        // Decrement button logic
        holder.btnDecrement.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(holder.quantityCounter.getText().toString());
            if (currentQty > 1) {
                holder.quantityCounter.setText(String.valueOf(currentQty - 1));
            }
        });

        // Add to cart logic
        holder.btnAddToCart.setOnClickListener(v -> {
            int requestedQuantity = Integer.parseInt(holder.quantityCounter.getText().toString());

            if (p.getQuantity() >= requestedQuantity) {
                for (int i = 0; i < requestedQuantity; i++) {
                    CartHelper.addToCart(context, p);
                }

                holder.quantityCounter.setText("1");
                Toast.makeText(context, requestedQuantity + " " + p.getName() + "(s) added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Sorry, not enough " + p.getName() + " in stock", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}