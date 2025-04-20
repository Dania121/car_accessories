package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {
    ImageView productImage;
    TextView productName, productPrice, productQuantity;

    public CartViewHolder(View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.cart_product_image);
        productName = itemView.findViewById(R.id.cart_product_name);
        productPrice = itemView.findViewById(R.id.cart_product_price);
        productQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }
}
