package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
    public ImageView productImage;
    public TextView productName, productPrice, productQuantity;
    public Button btnAddToCart;
    public ImageButton btnIncrement, btnDecrement;
    public TextView quantityCounter;

    public ProductViewHolder(View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        productQuantity = itemView.findViewById(R.id.product_quantity);
        btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        btnIncrement = itemView.findViewById(R.id.btn_increment);
        btnDecrement = itemView.findViewById(R.id.btn_decrement);
        quantityCounter = itemView.findViewById(R.id.quantity_counter);
    }
}