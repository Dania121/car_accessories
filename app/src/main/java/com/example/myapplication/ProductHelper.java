// ProductHelper.java
package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductHelper {
    private static final String PREF_NAME = "product_data";
    private static final String PRODUCTS_KEY = "products";
    private static final String VERSION_KEY = "products_version";

    private static final int CURRENT_VERSION = 2;

    // Initialize products from JSON file and save to SharedPreferences
    public static void initializeProductsFromJson(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int savedVersion = prefs.getInt(VERSION_KEY, 0);


        if (savedVersion < CURRENT_VERSION) {
            try {
                InputStream is = context.getAssets().open("products.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");

                List<Product> productList = new ArrayList<>();
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int quantity = obj.has("quantity") ? obj.getInt("quantity") : 10;
                    productList.add(new Product(
                            obj.getString("name"),
                            obj.getDouble("price"),
                            obj.getString("image"),
                            quantity
                    ));
                }


                Gson gson = new Gson();
                String productsJson = gson.toJson(productList);
                prefs.edit()
                        .putString(PRODUCTS_KEY, productsJson)
                        .putInt(VERSION_KEY, CURRENT_VERSION)
                        .apply();

            } catch (IOException | JSONException e) {
                Toast.makeText(context, "Error initializing products: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    // Get all products from SharedPreferences
    public static List<Product> getAllProducts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(PRODUCTS_KEY, null);

        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(json, type);
        }

        return new ArrayList<>();
    }

    // Save products to SharedPreferences
    private static void saveProducts(Context context, List<Product> products) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(products);
        prefs.edit().putString(PRODUCTS_KEY, json).apply();
    }

    // Decrement product quantity by specified amount
    public static void decrementProductQuantity(Context context, String productName, int amount) {
        List<Product> products = getAllProducts(context);
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                int newQuantity = Math.max(0, product.getQuantity() - amount);
                product.setQuantity(newQuantity);
                break;
            }
        }
        saveProducts(context, products);
    }

    // Update product quantity directly to a specific value
    public static void updateProductQuantity(Context context, String productName, int newQuantity) {
        List<Product> products = getAllProducts(context);
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                product.setQuantity(newQuantity);
                break;
            }
        }
        saveProducts(context, products);
    }
}